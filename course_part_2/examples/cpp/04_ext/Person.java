class Person {
	public void print() {
		System.out.println("person");
	}
	
	public static void main(String[] args) {
		Student s = new Student();
		Person p = s;
		
		p.print();
	}
	
}

class Student extends Person {
	public void print() {
		System.out.println("student");
	}
}
