//template<class T>
struct comparable {
    virtual int compare_to(/*const*/ comparable/*<T>*/&) = 0;  //pure virtual function
};

struct printable {
    virtual void print() = 0;  //pure virtual function
};
