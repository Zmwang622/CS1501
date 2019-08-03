// CS 1501 Summer 2019
// DLB Trie Node implemented as an external class which
// implements the TrieNodeInt<V> Interface

package TriePackage;

import java.util.*;
public class DLBNode<V> implements TrieNodeInt<V>
{
    protected Nodelet front; // front of linkedlist
    protected int degree; // num of  children
	protected V val; // word or nah
	
    protected class Nodelet
    {
    	protected char cval; // char, array alternative
    	protected Nodelet rightSib; // next in chain
    	protected TrieNodeInt<V> child; // node in array's index

        protected Nodelet(char inputCval)
        {
            cval = inputCval;
            rightSib = null;
            child = null;
        }

        protected Nodelet()
        {
            rightSib = null;
            child = null;
        }
        // Some helper methods I never used
        protected void setChild(TrieNodeInt<V> childNode)
        { 
            child = childNode;
        }

        protected DLBNode<V> getChild()
        {
            return (DLBNode<V>)child;
        }

        protected void setRight(Nodelet newRightSib)
        {
            assert rightSib != null;
            rightSib = newRightSib;
        }
    }
    		
	// You must supply the methods for this class.  See TrieNodeInt.java for the
	// interface specifications.  You will also need a constructor or two.
    public DLBNode()
    {
        front = null;
        val = null;
        degree = 0;
    }

    public DLBNode(V data)
    {
        front = null; 
        val = data;
        degree = 0;
    }

    public V getData()
    {
        return val;
    }

    public void setData(V data)
    {
        val = data;
    }

    public int getDegree()
    {
        return degree;
    }

    public int getSize()
    {
        Nodelet currentNodelet = front;
        int numNodes = 0;
        while(currentNodelet!=null) // we need to count the number of nodes in the chain...getDegree?
        {
            numNodes+=1;
            currentNodelet = currentNodelet.rightSib; // traverse along
        }

        int nodeSpace = numNodes*9; // each node holds 9 bytes
        int otherSpace = 4 + 4; // the two other pointers
        return nodeSpace + otherSpace; // return total size 
    }

    public TrieNodeInt<V> getNextNode(char c)
    {
        Nodelet currentNodelet = front; 
        while(currentNodelet != null) // traverse through the link until we find the child.
        {
            if(currentNodelet.cval==c) // found child
            {
                return currentNodelet.child;
            }
            currentNodelet = currentNodelet.rightSib; // chug along
        }
        return null; // no child contains c, return null.
    }

    public void setNextNode(char c, TrieNodeInt<V> nextNode)
    {
        if(front == null)
        {
            degree+=1;
            front = new Nodelet(c);
            front.child = nextNode;
            return;
        }

        if(front.cval>c)
        {
            degree+=1;
            Nodelet insert = new Nodelet(c);
            insert.child  = nextNode;
            insert.rightSib = front;
            front = insert;
            return;
        }

        Nodelet currentNodelet = front;
        while(currentNodelet.rightSib !=null && c > currentNodelet.cval)
        {
            if(currentNodelet.cval==c)
            {
                currentNodelet.child = nextNode;
                return;
            }
            currentNodelet = currentNodelet.rightSib;            
        }
        if(c<currentNodelet.cval)
        {
            degree+=1;
            Nodelet insert = new Nodelet(c);
            insert.child = nextNode;
            insert.rightSib = currentNodelet;
        }
        else if(c>currentNodelet.cval)
        {
            degree+=1;
            Nodelet insert = new Nodelet(c);
            insert.child =nextNode;
            currentNodelet.rightSib = insert;
        }
        else if (c == currentNodelet.cval)
        {
            currentNodelet.child = nextNode;
        }
        else
        {
            degree+=1;
            Nodelet insert = new Nodelet(c);
            insert.child = nextNode;
            currentNodelet.rightSib = insert;
        }
        // currentNodelet.rightSib = insert;
    }

    public Iterable<TrieNodeInt<V>> children() //similar to MTAlph Children search
    {
        Queue<TrieNodeInt<V>> childrenQ = new LinkedList<>();
        collect(this.front,childrenQ);
        return childrenQ;
    }

    private void collect(Nodelet x, Queue<TrieNodeInt<V>> q)
    {
        if(x==null)
        {
            return;
        }
        q.add(x.child);

        Nodelet curr = x;
        while(curr != null)
        {
            collect(curr.getChild().front,q);
            curr = curr.rightSib;
        }   
    }

    private void collect(TrieNodeInt<V> x, Queue<TrieNodeInt<V>> q)
    {
        if(x==null)
        {
            return;
        }
        DLBNode<V> dlb = (DLBNode<V>) x;
        Nodelet currentNodelet = dlb.front;
        while(currentNodelet!=null)
        {
            collect(currentNodelet.child,q);
            currentNodelet = currentNodelet.rightSib;
        }

    }
}
