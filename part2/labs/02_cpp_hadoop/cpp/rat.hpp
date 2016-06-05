class rat {
	/*
	 * n & d private for information hiding, no need to define getters/setters
	 * since they are accessed only in rat.cpp when implemnting public methods
	 * (n & d are visible in that case). */
	int n, d;
	public:
		rat(int, int);
		void print();
		
		/* 
		 * int& is used because it is reasonable that constants will never
		 * be passed, since the method basically copies tha values of n & d
		 * in two variables. */
		void unpack(int&, int&);
		
		/*
		 * rat& is used for efficiency, avoid copying the passed object.
		 * const is used because the parameter is not modified by methods,
		 * a new object representing the result is returned. */
		rat operator+(const rat&);
		rat operator-(const rat&);
		rat operator*(const rat&);
		rat operator/(const rat&);
};
