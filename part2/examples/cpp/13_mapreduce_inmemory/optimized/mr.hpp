#include <vector>
#include <algorithm>    //we need sorting
#include <iostream>

using namespace std;

/*
 * following types should be templates, to make this framework truly usable
 * in all kind of application
 * */
typedef int K1; //to make code more readable (and not use int everywhere)
typedef int V1;
typedef int K2;
typedef int V2;
typedef int K3;
typedef int V3;

/*
 * single write function with extra param to decide in which vector
 * to redirect the output
 * */
template <typename K, typename V>
void write(K& k, V& v, vector<pair<K,V> >& m) { //& for efficiency
    pair<K, V> p(k,v);
    m.push_back(p);
}

/*
 * we define map & reduce classes for generic application,
 * making their functions overridable
 * */
class mapper {
public:
    virtual void map(K1 k, V1 v, vector<pair<K2,V2> >& mid) = 0;
};

class reducer {
public:
    virtual void reduce(K2 k, vector<V2> l, vector<pair<K3,V3> >& out) = 0;
};

/*
 * generic debugging function to print a vector of pairs
 * */
template<typename K, typename V>
void print(vector<pair<K,V> > m) {
    
    /*
     * iterator depends from types K & V, we need to put typename to 
     * specify identify vector<...> as a type, otherwise we get:
     * 
     * mr.cpp: In function ‘void print(std::vector<std::pair<_T1, _T2> >)’:
        mr.cpp:65:10: error: need ‘typename’ before ‘std::vector<std::pair<_T1, _T2> >::iterator’ because ‘std::vector<std::pair<_T1, _T2> >’ is a dependent scope
        for (vector<pair<K,V> >::iterator it = m.begin(); it != m.end(); ++it) {
     * 
     * 
     * */
    for (typename vector<pair<K,V> >::iterator it = m.begin(); it != m.end(); ++it) {
        cout << (*it).first << " " << (*it).second << endl; //NOTE: first & second are fields, not functions
    }
}


class mapreduce {
    
    /*
     * encapsulate all variables in a single object (to get rid of 
     * global variables and make the program more clean)
     * */
     
    vector<pair<K1,V1> >& in;
    vector<pair<K2,V2> > mid;
    vector<pair<K2, vector<V2> > > reduce_in;
    vector<pair<K3,V3> >& out;
    mapper& m;
    reducer& r;
    
public:
    mapreduce(vector<pair<K1,V1> >& a, 
              vector<pair<K3,V3> >& b, 
              mapper& c,
              reducer& d) : in(a), out(b), m(c), r(d) {}
    
    void run() {
        for (vector<pair<K1,V1> >::iterator it = in.begin(); it != in.end(); ++it) {
            m.map((*it).first, (*it).second, mid); //NOTE: first & second are fields, not functions
        }
        
        //DEBUG
        cout << "input: " << endl;
        print<K1, V1>(in);
        
        /*
         * default ordering on pair is in key-value fashion (lexicographic)
         * exactly what we need (sort by key)
         * */
        sort(mid.begin(), mid.end());
        
        //DEBUG
        cout << "sorted intermediate: " << endl;
        print<K2, V2>(mid);
        
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
                    
            r.reduce((*it).first, (*it).second, out);
        } 
         
         
    }

};
