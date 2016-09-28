package finalllsssss;

public class Feature {

	String name;
	int grid[][];
	
	public Feature(String name, int grid[][]){
		this.name = name;
		this.grid = grid;
	}
	
	public Feature(String name, float grid[][]){
		this.name = name;
		
		this.grid = new int[grid.length][grid[0].length];
		for(int i = 0; i < grid.length; i++){
			for (int j = 0; j < grid[0].length; j++){
				this.grid[i][j] = (int) grid[i][j];
			}
		}
	}
	
	public float[][] getGrid(){
		float [][] result = new float[grid.length][grid[0].length];
		for(int i = 0; i < grid.length; i++){
			for (int j = 0; j < grid[0].length; j++){
				if(grid[i][j] == 0)
				result[i][j] = 1;
				else{
					result[i][j] = 0;
				}
			}
		}
		return result;
	}

}
