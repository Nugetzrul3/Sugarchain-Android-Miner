/*-
 * Copyright 2014 Alexander Peslyak
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted.
 *
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR AND CONTRIBUTORS ``AS IS'' AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED.  IN NO EVENT SHALL THE AUTHOR OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS
 * OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 */
#include "yescrypt.h"
#include "sha256.c"
#include "yescrypt-best.c"

#define YESCRYPT_N 2048
#define YESCRYPT_R 8
#define YESCRYPT_P 1
#define YESCRYPT_T 0
#define YESCRYPT_FLAGS (YESCRYPT_RW | YESCRYPT_PWXFORM)

static int yescrypt_bitzeny(const uint8_t *passwd, size_t passwdlen,
                            const uint8_t *salt, size_t saltlen,
                            uint8_t *buf, size_t buflen)
{
    static __thread int initialized = 0;
    static __thread yescrypt_shared_t shared;
    static __thread yescrypt_local_t local;
    int retval;
    if (!initialized) {
        /* "shared" could in fact be shared, but it's simpler to keep it private
         * along with "local".  It's dummy and tiny anyway. */
        if (yescrypt_init_shared(&shared, NULL, 0,
                                 0, 0, 0, YESCRYPT_SHARED_DEFAULTS, 0, NULL, 0))
            return -1;
        if (yescrypt_init_local(&local)) {
            yescrypt_free_shared(&shared);
            return -1;
        }
        initialized = 1;
    }
    retval = yescrypt_kdf(&shared, &local, passwd, passwdlen, salt, saltlen,
                          YESCRYPT_N, YESCRYPT_R, YESCRYPT_P, YESCRYPT_T,
                          YESCRYPT_FLAGS, buf, buflen);
#if 0
    if (yescrypt_free_local(&local)) {
        yescrypt_free_shared(&shared);
        return -1;
    }
    if (yescrypt_free_shared(&shared))
        return -1;
    initialized = 0;
#endif
    if (retval < 0) {
        yescrypt_free_local(&local);
        yescrypt_free_shared(&shared);
    }
    return retval;
}

static void yescrypt_hash(const char *input, char *output)
{
    yescrypt_bitzeny((const uint8_t *) input, 80,
                     (const uint8_t *) input, 80,
                     (uint8_t *) output, 32);
}

#include <stdbool.h>
struct work_restart {
	volatile unsigned long	restart;
	char			padding[128 - sizeof(unsigned long)];
};

extern struct work_restart *work_restart;
extern bool fulltest(const uint32_t *hash, const uint32_t *target);

static int pretest(const uint32_t *hash, const uint32_t *target)
{
	return hash[7] < target[7];
}

int scanhash_yescrypt(int thr_id, uint32_t *pdata, const uint32_t *ptarget,
		      uint32_t max_nonce, unsigned long *hashes_done)
{
	uint32_t data[20] __attribute__((aligned(128)));
	uint32_t hash[8] __attribute__((aligned(32)));
	uint32_t n = pdata[19] - 1;
	const uint32_t first_nonce = pdata[19];

	for (int i = 0; i < 20; i++) {
		be32enc(&data[i], pdata[i]);
	}
	do {
		be32enc(&data[19], ++n);
		yescrypt_hash((char *)data, (char *)hash);
		if (pretest(hash, ptarget) && fulltest(hash, ptarget)) {
			pdata[19] = n;
			*hashes_done = n - first_nonce + 1;
			return 1;
		}
	} while (n < max_nonce && !work_restart[thr_id].restart);
	
	*hashes_done = n - first_nonce + 1;
	pdata[19] = n;
	return 0;
}

