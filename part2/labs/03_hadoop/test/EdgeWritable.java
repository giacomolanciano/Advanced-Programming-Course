public class EdgeWritable /*implements Writable*/ {

	private int x, y;

	public EdgeWritable(int x, int y) {
		this.set(x,y);
	}
	
	public EdgeWritable() {
		this.set(0,0);
	}

	public int getX() { return x; }
	public int getY() { return y; }

	public void set(int x, int y) { 
		this.x = x; 
		this.y = y; 
	}

	/*
	@Override
	public void readFields(DataInput in) throws IOException {
		x = in.readInt();
		y = in.readInt();
	}

	@Override
	public void write(DataOutput out) throws IOException {
		out.writeInt(x);
		out.writeInt(y);
	}
	*/

	@Override
	public String toString() {
		//return "("+x+","+y+")";
		return x+","+y;
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		EdgeWritable pair = (EdgeWritable) o;

		boolean direct = true, inverse = true;
		
		if (getX() != pair.getX()) direct = false;
		if (getY() != pair.getY()) direct = false;
		
		if (getX() != pair.getY()) inverse = false;
		if (getY() != pair.getX()) inverse = false;
		
		return direct || inverse;

	}
	
	
	
	
	public static void main(String[] args) {
		EdgeWritable e; 
		EdgeWritable f;
		
		e = new EdgeWritable(0,1);
		f = new EdgeWritable(1,0);
		System.out.println(e + " equals " + f + " = " + e.equals(f));
		
		e = new EdgeWritable(1,0);
		f = new EdgeWritable(0,1);
		System.out.println(e + " equals " + f + " = " + e.equals(f));
		
		e = new EdgeWritable(0,0);
		f = new EdgeWritable(0,1);
		System.out.println(e + " equals " + f + " = " + e.equals(f));
		
		e = new EdgeWritable(0,0);
		f = new EdgeWritable(1,0);
		System.out.println(e + " equals " + f + " = " + e.equals(f));
	}
	
	
	
	
	
	
	
	
	
}
