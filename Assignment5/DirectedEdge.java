/*************************************************************************
 *  Compilation:  javac DirectedEdge.java
 *  Execution:    java DirectedEdge
 *
 *  Immutable weighted directed edge.
 *
 *************************************************************************/

/**
 *  The <tt>DirectedEdge</tt> class represents a weighted edge in an directed graph.
 *  <p>
 *  For additional documentation, see <a href="http://algs4.cs.princeton.edu/44sp">Section 4.4</a> of
 *  <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 */

public class DirectedEdge implements Comparable<DirectedEdge>{ 
    private final int v;
    private final int w;
    private double weight;

   /**
     * Create a directed edge from v to w with given weight.
     */
    public DirectedEdge(int v, int w, double weight) {
        this.v = v;
        this.w = w;
        this.weight = weight;
    }

   /**
     * Return the vertex where this edge begins.
     */
    public int from() {
        return v;
    }

   /**
     * Return the vertex where this edge ends.
     */
    public int to() {
        return w;
    }

   /**
     * Return the weight of this edge.
     */
    public double weight() { return weight; }

    public void setWeight(double weight)
    {
        this.weight = weight;
    }


   /**
     * Return a string representation of this edge.
     */
    public String toString() {
        return v + "-" + w + " " + String.format("%5.2f", weight);
    }


    // Edge comparer
    public int compareTo(DirectedEdge that)
    {
        if(this.weight() < that.weight())
        {
            return -1;
        }    
        else if(this.weight() > that.weight())
        {
            return +1;
        }
        else
        {
            return 0;
        }
    }


   /**
     * Test client.
     */
    public static void main(String[] args) {
        DirectedEdge e = new DirectedEdge(12, 23, 3.14);
        StdOut.println(e);
    }
}
