class rat {
    int n, d;
public:
    rat(int a, int b);
    rat operator+(const rat&);
    rat operator-(const rat&);
    rat operator*(const rat&);
    rat operator/(const rat&);
    void unpack(int&, int&);
    void print();
};
