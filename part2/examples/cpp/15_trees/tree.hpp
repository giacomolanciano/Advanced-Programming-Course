#include <stddef.h>
#include <vector>

/*
 * let's do a typedef before going for template, to symplify developing
 * (error messages are crazy with templates!)
 * */
typedef int T;

//template<>	//do later!
class node {
	T info;
	
	/*
	 * the tree has not a max number of children
	 * */
	std::vector<node*> children;
	friend class tree;
public:
	/*
	 * iterator -> on children of a node
	 * */
	class iterator {
		/*
		 * we basically need a vector iterator (boxing a vec iterator in
		 * a node iterator)
		 * */
		std::vector<node*>::iterator it;
		iterator(std::vector<node*>::iterator& i) : it(i) {}
		friend class node;	
	public:
		bool operator != (const iterator& other) {
			return it != other.it;
		}
		node*& operator * () {
			return *it;
		}
		iterator& operator ++ () {
			++it;
			
			/*
			 * return the reference to the obj itself
			 * */
			return *this;	
		}
	};
	iterator begin() {
		std::vector<node*>::iterator it = children.begin();
		iterator i(it);
		return i;
	}
	iterator end() {
		std::vector<node*>::iterator it = children.end();
		iterator i(it);
		return i;
	}
	T get() {
		return info;
	}
};

class tree {
	node* r;
public:
	/*
	 * constructor -> create empty tree
	 * */
	tree() : r(NULL) {}
	
	/*
	 * add -> add leaf to tree by specifiyng the parent node
	 * should return a pointer to the just inserted node
	 * */
	node* add(T info, node* parent) {
		node * v = new node;
		v->info = info;
		
		if(parent != NULL)
			parent->children.push_back(v);
		else
			r = v;
		
		return v;
	}
	
	node* add(T info) {
		return add(info, NULL);
	}
	
	/*
	 * root -> get root node
	 * */
	node* root() {
		return r;
	}
	
	/*
	 * remove -> remove subtree
	 * */
	void del(node* v) {
		/*
		 * delete nodes in post-order
		 * */
		if(v == NULL) 
			return;
		for(node::iterator it = v->begin(); it != v->end(); ++it) {
			del(*it);
		delete v;
	}
	}
	void remove(node* v) {
		if(r == v)
			r = NULL;
		del(v);
		
		/*
		 * EXERCISE: delete also the pointer in the vector of the parent
		 * node
		 * NOTE: we need a extra pointer to the parent node in node class
		 * */
	}
	
};





















