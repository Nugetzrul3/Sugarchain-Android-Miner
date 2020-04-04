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
#include "yespower.h"
#include "yespower.c"

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

void yespower_hash( const char *input, char *output, uint32_t len )
{
    static yespower_params_t params = {
        .N = 2048,
        .r = 32,
        .pers = "Now I am become Death, the destroyer of worlds",
        .perslen = 46
    };

    yespower_tls( (yespower_binary_t*)input, len, &params, (yespower_binary_t*)output );
}

int scanhash_power2b(int thr_id, uint32_t *pdata, const uint32_t *ptarget,
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
		yespower_hash((char *)data, (char *)hash, 80);
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

