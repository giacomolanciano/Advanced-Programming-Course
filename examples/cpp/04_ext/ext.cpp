#include <string.h>
#include <iostream>


struct person {
    char name[128];
    int age;
    virtual void print() {  //declared virtual to allow overriding
        std::cout << name << " " << age << "\n";
    }
};


struct student : public person {    //the way to extend person
    char id[16];
    void print() {
        std::cout << name << " " << age << " " << id << "\n";
    }
};


int main () {
    
    student s;
    s.age = 30;
    //s.name = "alice"; //can't do this
    strcpy(s.name, "alice");
    strcpy(s.id, "9615243");
    
    std::cout << "size of s: " << sizeof(s) << "\n"; //char + int + char = 148 -> if print() is virtual then size = 160 (???)
    
    
    std::cout << "addr of s.name: " << &s.name << "\n";
    std::cout << "addr of s.age:  " << &s.age << "\n";
    std::cout << "addr of s.id:   " << &s.id << "\n";
    
    //to print the address of the first byte after the end of the struct (1 counts as the dimension of the struct s -> offset in terms of the obj)
    std::cout << "addr of first free byte after s: " << (&s) + 1 << "\n";
    
    //NB it's like if the student has as prefix a person!!!
    person* p = &s;
    std::cout << "p->name: " << p->name << "\n";
    
    
    //i need to override print(), otherwise i call function declared in person
    s.print();
    
    /*
     * the reference is a person, but the obj is a student. 
     * in Java we have late binding, so in that case student's print is called (see Person.java)
     * in C++ we don't have late binding, we have to declare a method as virtual (in the superclass) to allow overriding */
     
    //person* p = &s;
    p->print(); 

    
    return 0;
}






















