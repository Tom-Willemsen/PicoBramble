package primes;

public final class PrimalityTests {
	
	private static final Long ZERO = Long.valueOf(0);
	private static final Long ONE = Long.valueOf(1);
	private static final Long SIX = Long.valueOf(6);
	
	/**
	 * Tests a number for being a prime.
	 * 
	 * @param n - the number to test
	 * @return true if the number is a prime, false otherwise
	 */
	public static boolean isPrime(Long n){

		return test_tiny_primes(n) && test_divisors(n);
	}
	
	/**
	 * Tests for a prime for n<10 by comparing to a list of known primes
	 * @param n - an integer less than 10.
	 * @return true if the number is a prime, false otherwise
	 */
	
	private static boolean test_tiny_primes(Long n){
		if(n%2L == 0 || n%3L == 0|| n%5L == 0 || n%7L == 0 || n < 0){
			return false;
		}
		return true;
	}
	
	/**
	 *  Tests for a prime by an exhaustive search for factors
	 *  
	 *  @return true if the number is a prime, false otherwise
	 *  
	 *  Optimisations:
	 *  - Only check factors up to sqrt(n)
	 *  - Only check for factors 2, 3, 6k-1 and 6k+1 (integer k)
	 */
	private static boolean test_divisors(Long n){
		
		final Long square_root_n = Long.valueOf((long) Math.ceil(Math.sqrt(n)));
		
		// Can start at i=2 because 5 & 7 are covered by check_tiny_primes()
		for(Long i = Long.valueOf(2); (SIX*i)-ONE<=square_root_n; i++){
			
			if(n%(SIX*i - ONE) == ZERO || n%(SIX*i + ONE) == ZERO){
				return false;
			}

		}
		
		return true;
	}
	
}