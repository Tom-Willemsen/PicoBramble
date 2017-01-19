package primes;

public final class PrimalityTests {
	
	public static boolean isPrime(Long n){

		if(n<10){
			return test_tiny_primes(n);
		} 
		
		return test_divisors(n);
	}
	
	public static boolean test_tiny_primes(Long n){
		if(n == 2 || n == 3 || n == 5 || n == 7){
			return true;
		} 
		return false;
	}
	
	/**
	 *  Tests for a prime by an exhaustive search for factors
	 *  
	 *  @return true if the number is a prime, false otherwise
	 *  
	 *  Optimisations:
	 *  - Only check factors up to sqrt(n)
	 *  - Only check for factors 2, 3, 6k-1 and 6k+1
	 */
	public static boolean test_divisors(Long n){
		if(n%2 == 0){
			return false;
		}
		if(n%3 == 0){
			return false;
		}
		
		double square_root_n = Math.sqrt(n);
		
		for(Long i = new Long(1); (6*i)-1<=square_root_n; i++){
			
			if(n%(6*i - 1) == 0){
				return false;
			}
			
			if(n%(6*i + 1) == 0){
				return false;
			}
		}
		return true;
	}
	
}
