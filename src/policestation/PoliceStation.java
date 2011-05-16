package policestation;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.Integer;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * School project for Y36TIN (Graph theory lessons)
 *
 * @author Ondřej Macoszek <ondra@macoszek.cz>
 */
public class PoliceStation
{
	public int maxLength = -1;
	public Set<Node> riskPlaces;
	public Set<Node> normalPlaces;
	public Map<Integer, Node> places = new HashMap<Integer, Node>();
	public Set<Connection> connections = new HashSet<Connection>();
	public Set<Connection> roads;
	public Set<Connection> paths;
	public int[][] distances;
	
	enum LineType { RiskPlaces, NormalPlaces, Roads, Paths }
	
	public void load(String path) throws FileNotFoundException, IOException
	{
		FileInputStream fin = new FileInputStream(path);
		DataInputStream din = new DataInputStream(fin);
		BufferedReader br = new BufferedReader(new InputStreamReader(din));
		
		String line;
		int lineNumber = 0;
		
		int linesToRead = -1;
		LineType type = null;
		int highestId = 0;
		
		while((line = br.readLine()) != null) {
			
			lineNumber++;
			
			if(lineNumber == 1) {
				maxLength = Integer.parseInt(line);
				continue;
			}
			
			if(lineNumber == 2) {
				linesToRead = Integer.parseInt(line);
				riskPlaces = new HashSet<Node>(linesToRead);
				type = LineType.RiskPlaces;
				continue;
			}
			
			if(type == LineType.RiskPlaces) {
				
				if(riskPlaces.size() < linesToRead) {
					Node node = new Node();
					node.isRisk = true;

					String[] s = line.split(" ");
					node.id = Integer.parseInt(s[0]);
					node.x = Integer.parseInt(s[1]);
					node.y = Integer.parseInt(s[2]);

					if(node.id > highestId) {
						highestId = node.id;
					}
					
					riskPlaces.add(node);
					places.put(node.id, node);
				}
				
				if(riskPlaces.size() == linesToRead) {
					type = LineType.NormalPlaces;
					linesToRead = -1;
				}
				
				continue;
			} 
			
			if(type == LineType.NormalPlaces) {
				
				if(linesToRead == -1) {
					linesToRead = Integer.parseInt(line);
					normalPlaces = new HashSet<Node>(linesToRead);
				} else if(normalPlaces.size() < linesToRead) {
					Node node = new Node();
					node.isRisk = false;

					String[] s = line.split(" ");
					node.id = Integer.parseInt(s[0]);
					node.x = Integer.parseInt(s[1]);
					node.y = Integer.parseInt(s[2]);

					if(node.id > highestId) {
						highestId = node.id;
					}
					
					normalPlaces.add(node);
					places.put(node.id, node);
				}
				
				if(normalPlaces.size() == linesToRead) {
					type = LineType.Roads;
					linesToRead = -1;
				}
				
				continue;		
				
			}
			
			if(type == LineType.Roads) {
				
				if(linesToRead == -1) {
					linesToRead = Integer.parseInt(line);
					roads = new HashSet<Connection>(linesToRead);
					distances = new int[highestId+1][highestId+1];
					
					for (int[] targets : distances) {
						targets = new int[highestId+1];
					}
					
					
				} else if(roads.size() < linesToRead) {

					String[] s = line.split(" ");
					
					Connection c = new Connection();
					c.n1 = places.get(Integer.parseInt(s[0]));
					c.n2 = places.get(Integer.parseInt(s[1]));
					c.distance = Integer.parseInt(s[2]);
					c.updateNodeNeighbors();
					
					distances[c.n1.id][c.n2.id] = c.distance;
					distances[c.n2.id][c.n1.id] = c.distance;
					
					connections.add(c);
					roads.add(c);
				
				}
				
				if(roads.size() == linesToRead) {
					type = LineType.Paths;
					linesToRead = -1;
				}
				
				continue;		
				
			}
			
			if(type == LineType.Paths) {
				
				if(linesToRead == -1) {
					linesToRead = Integer.parseInt(line);
					paths = new HashSet<Connection>(linesToRead);
					
				} else if(paths.size() < linesToRead) {

					String[] s = line.split(" ");
					
					Connection c = new Connection();
					c.n1 = places.get(Integer.parseInt(s[0]));
					c.n2 = places.get(Integer.parseInt(s[1]));
					c.computeDistanceFromNodeCoords();
					c.updateNodeNeighbors();
					
					distances[c.n1.id][c.n2.id] = c.distance;
					distances[c.n2.id][c.n1.id] = c.distance;
					
					connections.add(c);
					paths.add(c);
				
				}
				
				if(paths.size() == linesToRead) {
					linesToRead = -1;
					break;
				}
			}
			
		}
		
		din.close();
	}
	
	public void performDijkstra(Node start)
	{
		// mark all nodes with infinity distance
		for(Node node : places.values()) {
			node.currentDistanceFromTarget = Integer.MAX_VALUE;
		}
		start.currentDistanceFromTarget = 0;
		
		// init for no closed nodes
		Set<Node> closed = new HashSet<Node>(places.size());
		
		// init priority queue
		Set<Node> q = new HashSet<Node>(places.size());
		q.add(start);
		
		while(!q.isEmpty()) {
			
			Node nearestNode = null;
			int minimalDistance = Integer.MAX_VALUE;
			for(Node node : q) {
				if(node.currentDistanceFromTarget < minimalDistance) {
					minimalDistance = node.currentDistanceFromTarget;
					nearestNode = node;
				}
			}
		
			q.remove(nearestNode);
			closed.add(nearestNode);
			
			for(Node neigbor : nearestNode.neighbors) {
				if(!closed.contains(neigbor)) {
					
					// relax
					if( 
					    neigbor.currentDistanceFromTarget 
					    > 
					    nearestNode.currentDistanceFromTarget 
					    + distances[nearestNode.id][neigbor.id]
					) {
						// shorten distance
						neigbor.currentDistanceFromTarget =
						  nearestNode.currentDistanceFromTarget 
						  + distances[nearestNode.id][neigbor.id];
						
						// add again to queue
						q.add(neigbor);
					}	

				}
			}
			
		}
	}
	public List<Node> getNearestPolicemen(Node start)
	{
		List<Node> result = new LinkedList<Node>();
		
		performDijkstra(start);
		
		for(Node node : riskPlaces) {
			if(node.currentDistanceFromTarget <= maxLength) {
				result.add(node);
			}
		}
		result.remove(start);
		
		return result;
	}
	
	public void solve()
	{
		for (Node node : riskPlaces) {
			List<Node> nearest = getNearestPolicemen(node);
			printNodes(nearest, "Starting from #"+node.id);
		}
	}
	
	public void printNodes(List<Node> list, String title)
	{
		System.out.println("---------------------------");
		System.out.println(title);
		if(list.isEmpty()) {
			System.out.println("no policemen within distance of "+maxLength);
		}
		for(Node node : list) {
			System.out.print(node.id + ", ");
		}
		System.out.println("");
	}

	public static void main(String[] args) throws FileNotFoundException, IOException
	{
		PoliceStation station = new PoliceStation();
		
		station.load("./data.txt");
		station.solve();
		
	}
}