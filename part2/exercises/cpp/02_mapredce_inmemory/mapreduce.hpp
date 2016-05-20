#include <vector>
#include <algorithm>
#include <iostream>


/*
 * generic debugging function to print a vector of pairs
 * */
template<typename K, typename V>
void print(std::vector<std::pair<K,V> > m) {
    
    /*
     * iterator depends from types K & V, we need to put typename to 
     * identify vector<...> as a type, otherwise we get:
     * 
     * mr.cpp: In function ‘void print(std::vector<std::pair<_T1, _T2> >)’:
        mr.cpp:65:10: error: need ‘typename’ before ‘std::vector<std::pair<_T1, _T2> >::iterator’ because ‘std::vector<std::pair<_T1, _T2> >’ is a dependent scope
        for (vector<pair<K,V> >::iterator it = m.begin(); it != m.end(); ++it) {
     * 
     * 
     * */
    for (typename std::vector<std::pair<K,V> >::iterator it = m.begin(); it != m.end(); ++it) {
        std::cout << (*it).first << " " << (*it).second << std::endl;
    }
}


/*
 * single write function with extra param to decide in which vector
 * to redirect the output
 * */
template <typename K, typename V>
void write(K& k, V& v, std::vector<std::pair<K,V> >& m) {
    std::pair<K, V> p(k,v);
    m.push_back(p);
}


/*
 * define map & reduce classes for generic application,
 * making their functions overridable
 * */
template <typename K1, typename V1, typename K2, typename V2>
class mapper {
public:
    virtual void map(K1& k, V1& v, std::vector<std::pair<K2,V2> >& mid) = 0;    
};

template <typename K2, typename V2, typename K3, typename V3>
class reducer {
public:
    virtual void reduce(K2& k, std::vector<V2>& l, std::vector<std::pair<K3,V3> >& out) = 0;
};


/*
 * define a class that encapsulate the execution logic of MapReduce
 * */
template <typename K1, typename V1, typename K2, typename V2, typename K3, typename V3>
class mapreduce {
    
    /*
     * shortcuts to make the code more readable
     * */
    typedef std::pair<K1,V1>               map_in_t;
    typedef std::pair<K2,V2>               map_out_t;
    typedef std::pair<K2,std::vector<V2> > reduce_in_t;
    typedef std::pair<K3,V3>               reduce_out_t;
    
    /*
     * private fields
     * */
    bool                        debug;
    std::vector<map_in_t>&      in;
    std::vector<map_out_t>      mid;
    std::vector<reduce_in_t>    reduce_in;
    std::vector<reduce_out_t>&  out;
    mapper<K1,V1,K2,V2>&        m;
    reducer<K2,V2,K3,V3>&       r;
    
public:
    mapreduce(std::vector<map_in_t>& a, 
              std::vector<reduce_out_t>& b, 
              mapper<K1,V1,K2,V2>& c,
              reducer<K2,V2,K3,V3>& d) : in(a), out(b), m(c), r(d), debug(false) {}
    
    void set_debug(bool b) {
        debug = b;
    }
    
    void run() {
        
        //DEBUG
        if(debug)  {
            std::cout << "input: " << std::endl;
            print<K1, V1>(in);
        }
        
        /*
         * map phase
         * */
        for (typename std::vector<map_in_t>::iterator it = in.begin(); it != in.end(); ++it) {
            m.map((*it).first, (*it).second, mid);
        }
        
        /*
         * default ordering on pair is in key-value fashion (lexicographic)
         * exactly what we need (sort by key)
         * */
        sort(mid.begin(), mid.end());
        
        //DEBUG
        if(debug)  {
            std::cout << "sorted intermediate: " << std::endl;
            print<K2, V2>(mid);
        }
        
        /*
         * group values by same key
         * */
        K2 prev;
        reduce_in_t p;
        bool first_iter = true;
        for (typename std::vector<map_out_t>::iterator it = mid.begin(); it != mid.end(); ++it) {
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
                if(debug)  {
                    //cout << "starting new group for key " << p.first << endl;
                }
            } 
            
            p.second.push_back((*it).second);
            prev = (*it).first;
        }
        if(!first_iter) {
            /*
             * still need to put last group in reducer input
             * if mid is empty then do not execute this instruction
             * */
            reduce_in.push_back(p);
        }
        
        
        /*
         * reduce phase
         * */
        for (typename std::vector<reduce_in_t>::iterator it = reduce_in.begin(); 
                it != reduce_in.end(); ++it) {
                    
            r.reduce((*it).first, (*it).second, out);
        }
        
        //DEBUG
        if(debug)  {
            std::cout << "output: " << std::endl;
            print<K3,V3>(out); 
        }
         
    }

};
