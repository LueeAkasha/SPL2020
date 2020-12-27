#ifndef SESSION_H_
#define SESSION_H_

#include <vector>
#include <string>
#include <queue>
#include "Graph.h"

class Agent;

enum TreeType{
    Cycle,
    MaxRank,
    Root
};

class Session{
public:
    Session(const std::string& path); // constructor
    Session(const Session &other); // copy constructor
    Session(Session &&other); // move constructor.
    const Session & operator=(const Session &other); //copy assign operator.
    const Session & operator=(Session &&other); // move assign operator
    virtual ~Session(); // destructor.

    void simulate(); // starting the simulate.
    void addAgent(const Agent& agent); // adding an agent to the session.
    void setGraph(const Graph& graph); // setting a graph to the session.
    void enqueueInfected(int); // enqueue an infected node to the infected queue.
    int dequeueInfected(); // dequeue an infected node to treat with.
    TreeType getTreeType() const; // returns the type of the trees in the session.
    int getCurrCycle() const; // returns the number of the current cycle.
    bool isOccupied(int node); // checking if a node is occupied by a virus.
    void setAsOccupied(int node); // signing a node as an occupied.
    bool isInfected(int node); // checking if a node is attacked in the graph.
    void Disconnect(int node); // disconnecting a node from the graph.
    Graph & GetGraph(); // returns the graph.
    bool isTerminated(); // checking if shall we stop the simulation due to the conditions .
    bool isDisconnected(int node); // checking if a node is connected to the graph.
    bool ThereIsInfected(); // checking if there are more attacked nodes to treat with.

private:
    Graph g; // the network.
    TreeType treeType; // type of trees in the session.
    std::vector<Agent*> agents; // list of agents.
    std::vector<bool> occupiedNodes; // signing if a node was occupied.
    std::queue<int> infectedNodes; // the infected nodes queue.
    std::vector<bool>disconnectedNodes; // signing if a node was disconnected or not.
    int currCycle; // the number of current cycle in simulation.
    void advanceCycle(); // jumping to the next cycle.
    void init(int Size); // initialize the lists with the negative or default values.
    void WriteToJson(); // writing to output.json at the end of simulation.
    void clear(); // delete the resources in the session.
};

#endif
