#include <vector>
#include <algorithm>
#include "point.hpp"

using namespace std;

int main() {
    
    point p1(2.5, 3.7), p2(3.1, 1.1);
    vector<point> v;    //dynamic array
    //vector<printable> v;    //dynamic array
    v.push_back(p1);
    v.push_back(p2);
    v.push_back(p1);
    v.push_back(p2);
    
    
    for(int i=0; i<v.size(); i++) v[i].print();
    
    //for(vector<printable>::iterator it = v.begin(); it != v.end(); it++)
    for(vector<point>::iterator it = v.begin(); it != v.end(); it++)
        (*it).print();
    
    
    
    /* 
     * iterator redefine operator +, to offset end and start
     * in the example end+3 is excluded
     * since we access memory outside the vector seg fault may occurs*/
    //for(vector<point>::iterator it = v.begin()+2; it != v.end()+3; it++)
        //(*it).print();
    
    /* 
     * sorting alg*/
    vector<int> q;
    for(int i=0; i<20; i++) q.push_back(i%3);
    
    sort(q.begin(), q.end());
    
    for(int i=0; i<20; i++) cout << q[i] << endl;
    
    
    
    return 0;
}
