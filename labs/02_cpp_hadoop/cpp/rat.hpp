

class rat {
    public:
        int n, d;
        rat(int, int );
        void print();
        void unpack(int&, int&);
        rat operator+(rat);
        rat operator-(rat);
        rat operator*(rat);
        rat operator/(rat);
};
