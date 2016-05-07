#include <stddef.h>


//old implementation can be found in stack.cpp

/*
template <class T>
class node;
*/

template <class T>
class node {
    public:
        T elem;
        node<T>* next;
};

/*
template <class T>
class stack {
    node<T>* head;
    public:
        stack() : head(NULL) {};
        bool isempty();
        void push(T); //T& for efficiency
        T pop();
        T top();
};
*/



template <class T>
class stack {
    node<T>* head;
    public:
        stack() : head(NULL) {};
        bool isempty() {
            return head == NULL;
        }
        void push(T elem) { //T& for efficiency
            node<T>* n = new node <T>;
            n->elem = elem; 
            n->next = head;
            head = n;
        }
        T pop() {
            T elem = head->elem;
            node<T>* dead = head;
            head = head->next;
            delete dead;
            return elem;
        }
        T top() {
            return head->elem;
        }
};
