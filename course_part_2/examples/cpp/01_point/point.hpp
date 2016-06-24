class point {
	double x,y;
	
	public:
		point(double, double);
		point(double);
		~point();
		void print();
		point sum(point);
		point operator+(point);
};
