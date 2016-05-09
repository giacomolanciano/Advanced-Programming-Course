//#include<stdio.h>
#include<iostream>

// ./a.out to run

//using namespace std; // using this u don't need to write std::cout << "hello"; but u just write cout << "hello";

/*int main(){

	printf("hello \n");
	cout << "hello";
	
	return 0;

}*/




struct point{   // it s like a java class u can declare both variables and member function(which is how u call methods in c++)

	double x,y;
	void print() {  // like a java toString()
		std::cout << x  << " " << y << "\n" ;
	}
};

//the new main is


/*int main(){
	
	struct point p = {3.5 , 3.9}; // u can also omit struct
	p.x = 0.2; // how to access to the object
	p.print();
	std::cout << sizeof(p) << "\n";  // will print 16, the size of a double is 8
	
	// now let's dinamically create the object
	
	point* p2;
	p2=new point;
	p2 -> x = 3.4;
	p2 -> y = 5.4;
	p2 -> print();
	
	// here i am allocating smth but i am not deallocating
	
	delete p; // delete if you use new, free if you use malloc
	return 0;
}*/

// we can define a constructor in our struct by writing 
struct point2{   

	double x,y;
	point2 (double a, double b){
	x=a;
	y=b;
	}
	
	//point2 (double a, double b): x(a), y(b){}  faster way to implement the constructor

	
	point2 (double a){ // u can have overloading
	x=a;
	y=0;
	}
	
	//we have also a destructor that is called when the obj is destroyed or when the variable goes out of scope
	
	~point2 () { // called after delete
		std::cout << " I lannister ti mandano i saluti \n " ;
	}

	void print() {  // like a java toString()
		std::cout << x  << " " << y << "\n" ;
	}
};

// u can call it in the usual java way point2= new point2(3.4, 5.4)


int main(){

	point2 p(3.4);
	p.print();
	
	return 0;

}
// in c++ u usually declare objects in this way, avoiding the new-delete commands


// in c++ u can declare as private only a subsection of a struct  doing this 

struct point2{   

	point2 (double a, double b){
	x=a;
	y=b;
	}
	
	point2 (double a){
	x=a;
	y=0;
	}
	void print() { 
		std::cout << x  << " " << y << "\n" ;
	}
	
	private : //all the stuff under this are private
		double x,y;

};

// As  a consequence u can no longer access with p.x,  p.y

// instead of struct u can use class. The difference is that in a struct everything is public as default, while in class the default is
// private also the functions. u can use public : to make them public

// in c++ in a strcut u can also only declare a function and define it elsewhere
// for example in point2 u declare void print() and then u write out of everything

// void point ;; print() void print() { 
//		std::cout << x  << " " << y << "\n" ;
//	}




// u can declare classes in other files with .hpp extension (for security ). Then u can include it with " #include "name_of_the_class_file.hpp"   
//( u use " for the ones u define and < > for the ones predefined by the system)  and then  u use with the same name u call the file

// the structure is : u write the .hpp with all the function only declared. Then u create the file .cpp specifying the functions
// where u do #include "class_name.hpp", and then u use them in the other classes with #include "class_name.hpp" and #include "class_name.cpp"


// c++ u can have define member function
// in the .hpp file u can write a function
// point sum(point, point);
// if u want to use it referencing as + u can call it
// point operator+ (point) 

