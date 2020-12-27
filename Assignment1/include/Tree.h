#ifndef TREE_H_
#define TREE_H_

#include <vector>

class Session;

class Tree{
public:
    Tree(int rootLabel); // constructor
    Tree(const Tree &other); // copy constructor
    Tree(Tree &&other); // move constructor
    const Tree & operator=(const Tree &other); // copy assign operator
    const Tree & operator=(Tree &&other); // move assign operator
    virtual ~Tree(); // destructor
    virtual Tree* clone() const=0; // abstract cloning function
    virtual int traceTree()=0; // abstract traceTree function.
    void addChild(const Tree& child); // adding a child to the children of the tree.
    static Tree* createTree(const Session& session, int rootLabel); // create a new tree due to its type in the session.
    int Ranking(); // returning the rank of tree.
    int getNode() const; // returning the root of the tree.
     Tree* getChild(int i); // returning a child by its number.
protected:
    int node;
    std::vector<Tree*> children;
    int RecursionTraceTree(int c); // helping function.
    std::vector<Tree *> getChildren(); // returning the children of a tree.
    void clear(); // cleaning the tree "reset".

};


class CycleTree: public Tree{
public:
    CycleTree(int rootLabel, int currCycle); // constructor
    virtual ~CycleTree();// destructor
    virtual Tree * clone() const; // returning a clone of the tree.
    virtual int traceTree();// acting due to treeType.

private:
    int currCycle;
};

class MaxRankTree: public Tree{
public:
    MaxRankTree(int rootLabel); // constructor
    virtual ~MaxRankTree();// destructor
    virtual Tree * clone() const; // returning a clone of the tree.
    virtual int traceTree();// acting due to treeType.


};

class RootTree: public Tree{
public:
    RootTree(int rootLabel); // constructor
    virtual ~RootTree();  // destructor
    virtual Tree * clone() const; // returning a clone of the tree.
    virtual int traceTree(); // acting due to treeType.

};

#endif
