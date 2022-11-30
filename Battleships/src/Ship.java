
public  class Ship {
	
	 int hp;
	 int length;
	//Suntetagmenes sto pinaka
	 int Xpos;
	 int Ypos;
	//Suntetagmenes gia to friendlyBoard
	 int x,y;
	 String name;
	 int lastCell;
	boolean vertical;
	
	public Ship (int ShipXpos, int ShipYpos,boolean isVertical,String aName,int length) {
		vertical = isVertical;
		Xpos=ShipXpos;
		Ypos = ShipYpos;
		name = aName;
		this.length=length;
		hp=length;
		x=Xpos*30;
		y=Ypos*30;
	}
	
	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

	public void setXpos(int xpos) {
		Xpos = xpos;
	}

	public void setYpos(int ypos) {
		Ypos = ypos;
	}

	public int getLength() {
		return length;
	}
	
	public int getX() {
		return x;
	}


	public int getY() {
		return y;
	}
	
	public String getName() {
		return name;
	}

	public void move(int newX,int newY) {
		Xpos = newX;
		Ypos = newY;
		x = Xpos * 30;
		y = Ypos * 30;
	}
	
	public int getLastCell() {
		lastCell = x + (length * 30);
		
		return lastCell;
	}
	
	public void PrintShipName () {
		System.out.println(name);
	}
	
	public boolean isHit(int hitX, int hitY) {
		if(vertical==true) {
			for(int i=Xpos; i<(Xpos+length); i++) {
				if(hitX == i && hitY==Ypos) {
					hp--;
					return true;
				}
			}
		}
		else 
		{
			for(int j=Ypos; j<(Ypos+length);j++)
			{
				if(hitY == j && hitX == Xpos)
				{
					hp--;
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean isDead() {
		if(hp==0)
			return true;
		return false;
	}
}
