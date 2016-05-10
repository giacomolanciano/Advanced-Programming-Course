//template<class T>
struct comparable {
    /*
     * we */
    virtual int compare_to(/*const*/ comparable/*<T>*/&) = 0;  //pure virtual function
};

struct printable {
    void print();
};
