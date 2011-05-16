package policestation;

/**
 *
 * @author Ondřej Macoszek <ondra@macoszek.cz>
 */
public class Connection
{
	public Node n1;
	public Node n2;
	public int distance;
	
	public void updateNodeNeighbors()
	{
		n1.neighbors.add(n2);
		n2.neighbors.add(n1);
	}
	
	public void computeDistanceFromNodeCoords()
	{
		distance = (int) Math.ceil(
			Math.sqrt(
				Math.pow(n1.x + n2.x, 2)
			      + Math.pow(n1.x + n2.x, 2)
			)
		);
	}
}
