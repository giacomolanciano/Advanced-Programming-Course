#include "mr.hpp"

/*
 * we define mapper & reducer for WORDCOUNT example
 * */

class my_mapper : public mapper {
    void map(K1 k, V1 v, vector<pair<K2,V2> >& mid) {
        /*
         * write (v,1) in mid
         * */
        int one = 1;
        write(k, one, mid);
    }
};


class my_reducer : public reducer {
    void reduce(K2 k, vector<V2> l, vector<pair<K3,V3> >& out) {
        int sum = 0;
        for (vector<V2>::iterator it = l.begin(); it != l.end(); ++it) {
            sum += *it;
        }
        
        /*
         * write (k,sum) in out
         * */
        write(k, sum, out);
    }
};



int main() {
    
    vector<pair<K1,V1> > in;
    vector<pair<K3,V3> > out;
    my_mapper m;
    my_reducer r;

    //fill in with data...
    for (int i = 0; i <10; i++) {
        pair<K1, V1> p(i%3, 0); //keys: 0, 1, 2, 0, 1, 2, 0, 1, 2, 0
        in.push_back(p);
    }
    
    mapreduce mr = mapreduce(in, out, m, r);
    
    /*
     * every side-effect on out made in run() will be visible in main()
     * because we pass the parameter as reference in the constructor
     * */
    mr.run();
    
    // read output data from out...
    cout << "output: " << endl;
    print<K3, V3>(out);
    
    return 0;
}



























