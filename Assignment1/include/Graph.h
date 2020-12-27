#ifndef GRAPH_H_
#define GRAPH_H_

#include <vector>
#include <queue>
#include "Tree.h"

class Graph{
public:
    Graph(std::vector<std::vector<int>> matrix); // constructor.
    Graph(const Graph &other); // copy constructor.
    virtual ~Graph() = default; // destructor.
    void infectNode(int nodeInd); // attacking a node.
    bool isInfected(int nodeInd); // checking if a node was attacked.
    std::vector<int> getInfectedNodes(); // returning the whole infected nodes.
    std::vector<std::vector<int>> GetEdges(); // getting the adjacency matrix.
    Tree *BFS(Session &session, int startNode); // creating BFS trees to treat with contactTracers.
    std::vector<int> DFS(int startNode); // DFS to get all the connected components in the graph.
    std::vector<int> getNeighbors(int nodeInd); // returning the neighbors of a node.
    void SetEdges(std::vector<std::vector<int>> vector); // setting new adjacency matrix to the graph.

private:
    std::vector<std::vector<int>> edges; // adjacency matrix.
    std::queue<int> infectedNodes; // attacked nodes.
    std::vector<bool> isInfectedNodes; // attacked nodes checker.
    void init(int size); // initializing the fields.
    Tree* BFS_helper(Session &session, int startNode, std::vector<bool> visited); //BFS
    std::vector<int> DFS_helper(int startNode, std::vector<bool> visited); // DFS
};

#endif