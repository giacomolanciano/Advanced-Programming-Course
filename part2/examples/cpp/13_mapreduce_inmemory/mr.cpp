#include <vector>
#include <algorithm>    //we need sorting
#include <iostream>

using namespace std;

typedef int K1; //to make code more readable (not use int always)
typedef int V1;
typedef int K2;
typedef int V2;
typedef int K3;
typedef int V3;

/*
 * we could use typedef also for the following types to make code more readable
 * */
vector<pair<K1,V1> > in;
vector<pair<K2,V2> > mid;
vector<pair<K2, vector<V2> > > reduce_in;
vector<pair<K3,V3> > out;


/*
 * we should have a single write function with extra param to decide if we use it in map or reduce
 * */

void map_write(K2 k, V2 v) {
    pair<K2, V2> p(k,v);
    mid.push_back(p);
}

void reduce_write(K3 k, V3 v) {
    pair<K3, V3> p(k,v);
    out.push_back(p);
}

/*
 * we define map & reduce functions for wordcount example
 * */
 
void map(K1 k, V1 v) {
    /*
     * write (v,1) in mid, to make it simpler, we make side-effect on global variable (wrong)
     * */
    map_write(v, 1);
}

void reduce(K2 k, vector<V2> l) {
    int sum = 0;
    for (vector<V2>::iterator it = l.begin(); it != l.end(); ++it) {
        sum += *it;
    }
    
    /*
     * write (v,1) in mid, to make it simpler, we make side-effect on global variable (wrong)
     * */
    reduce_write(k, sum);
}

void run() {
    for (vector<pair<K1,V1> >::iterator it = in.begin(); it != in.end(); ++it) {
        map((*it).first, (*it).second); //NB first & second are not functions
    }
    
    /*
     * default ordering on pair is in key-value fashion (lexicographic)
     * exactly what we need (sort by key)
     * */
    sort(mid.begin(), mid.end());
    
    /*
     * now we have to group values
     * */
}


int main() {
    //fill in with data...
    run();
    // read output data from out..
    return 0;
}



























