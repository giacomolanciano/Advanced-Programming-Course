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
    map_write(k, 1);
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

/*
 * generic debugging function
 * */
template<typename K, typename V>
void print(vector<pair<K,V> > m) {
    
    /*
     * iterator depends from types K & V, we need to put typename to specific identify vector<...> as a type
     * otherwise we get:
     * 
     * mr.cpp: In function ‘void print(std::vector<std::pair<_T1, _T2> >)’:
        mr.cpp:65:10: error: need ‘typename’ before ‘std::vector<std::pair<_T1, _T2> >::iterator’ because ‘std::vector<std::pair<_T1, _T2> >’ is a dependent scope
        for (vector<pair<K,V> >::iterator it = m.begin(); it != m.end(); ++it) {
     * 
     * 
     * */
    for (typename vector<pair<K,V> >::iterator it = m.begin(); it != m.end(); ++it) {
        cout << (*it).first << " " << (*it).second << endl; //NB first & second are fields, not functions
    }
}

void run() {
    for (vector<pair<K1,V1> >::iterator it = in.begin(); it != in.end(); ++it) {
        map((*it).first, (*it).second); //NB first & second are fields, not functions
    }
    
    //DEBUG
    //print<K1, V1>(in);
    
    /*
     * default ordering on pair is in key-value fashion (lexicographic)
     * exactly what we need (sort by key)
     * */
    sort(mid.begin(), mid.end());
    
    //DEBUG
    //print<K2, V2>(mid);
    
    /*
     * now we have to group values by same key
     * */
    pair<K2,vector<V2> > p; //group data for a given key
    K2 prev;
    bool first_iter = true;
    for (vector<pair<K2,V2> >::iterator it = mid.begin(); it != mid.end(); ++it) {
        /*
         * each group is a run. once we consume a run we flush p
         * */
        if(first_iter || prev != (*it).first) {
            if (!first_iter) {
                reduce_in.push_back(p);
            }
            first_iter = false;
            p.first = (*it).first;
            p.second.clear();   //flush
            
            //DEBUG
            //cout << "starting new group for key " << p.first << endl;
        } 
        
        p.second.push_back((*it).second);   //value is added to the list of values of p
        prev = (*it).first;
    }
    if(!first_iter) {
        /*
         * if mid is empty we do not want to execute this instruction
         * */
        reduce_in.push_back(p);
    }
    
    
    
    /*
     * reduce phase
     * */
    for (vector<pair<K2,vector<V2> > >::iterator it = reduce_in.begin(); 
            it != reduce_in.end(); ++it) {
                
        reduce((*it).first, (*it).second);
    } 
     
     
}


int main() {
    //fill in with data...
    for (int i = 0; i <10; i++) {
        pair<K1, V1> p(i%3, 0); //keys: 0, 1, 2, 0, 1, 2, 0, 1, 2, 0
        in.push_back(p);
    }
    
    run();
    
    // read output data from out... (print function can be used)
    for (vector<pair<K3,V3> >::iterator it = out.begin(); it != out.end(); ++it) {
        cout << (*it).first << " " << (*it).second << endl;
    }
    return 0;
}



























