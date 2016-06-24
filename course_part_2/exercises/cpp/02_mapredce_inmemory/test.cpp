#include "mapreduce.hpp"

using namespace std;

class my_mapper: public mapper<double,int,double,int> {
	void map(double& k, int& v, vector<pair<double,int> >& out) {
		int one = 1;
		write(k, one, out);
	}
};

class my_reducer: public reducer<double,int,double,int> {
	void reduce(double& k, vector<int>& l, vector<pair<double,int> >& out) {
		int sum = 0;
		for (vector<int>::iterator it = l.begin(); it != l.end(); ++it)
			sum += *it;
		write(k, sum, out);
	}
};

int main() {

	my_mapper m;
	my_reducer r;
	vector<pair<double,int> > in;
	vector<pair<double,int> > out;
	mapreduce<double,int,double,int,double,int> mr = 
		mapreduce<double,int,double,int,double,int>(in, out, m, r);

	// fill in with data...
	for (int i=0; i<10; i++) {
		pair<double,int> p(i%3+0.5, 0);
		in.push_back(p);
	}
	
	mr.set_debug(true);	
	mr.run();

	return 0;
}
