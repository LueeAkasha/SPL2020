//
// Created by zoix on 14/11/2020.
//
#include <fstream>
#include <iostream>
#include "../include/json.hpp"
#include "../include/Session.h"
#include "../include/Agent.h"
using namespace std;
using json = nlohmann::json;

Session::Session(const std::string &path)  : g(vector<vector<int>>()),treeType(Root) , agents(std::vector<Agent*>()) , occupiedNodes(vector<bool>()),infectedNodes(std::queue<int>()), disconnectedNodes(vector<bool>()) ,currCycle(0)  {

    //reading from json file.
    std::ifstream i(path);
    json j;
    i >> j;

    //initializing the tree type from json.
    if (j["tree"] == "M") {
        treeType = MaxRank;
    } else if (j["tree"] == "C") {
        treeType = Cycle;
    } else if (j["tree"] == "R") {
        treeType = Root;
    }

    //init the graph.
    std::vector<std::vector<int>> adjacencyMatrix = j["graph"];
    g = Graph(adjacencyMatrix);


    //init other resourses.
    init(adjacencyMatrix.size());


    //initializing the agents due their types.
    for (auto &agent : j["agents"]) {
        if (agent[0] == "V") {
            Agent * virus = new Virus(agent[1]);
            agents.push_back(virus);
            setAsOccupied(agent[1]);

        } else if (agent[0] == "C") {
            Agent * contactTeracer = new ContactTracer();
            agents.push_back(contactTeracer);
        }
    }
}

void Session::init(int Size){
    for (int i = 0; i < Size; ++i) {
        occupiedNodes.push_back(false);
        disconnectedNodes.push_back(false);
    }
}

Session::Session(const Session &other) : g(other.g), treeType(other.treeType) , agents(other.agents), occupiedNodes(other.occupiedNodes), infectedNodes(other.infectedNodes) , disconnectedNodes(other.disconnectedNodes) , currCycle(other.currCycle)  {
}

Session::Session(Session &&other) :g(other.g), treeType(other.treeType) , agents(other.agents) , occupiedNodes(other.occupiedNodes), infectedNodes(other.infectedNodes), disconnectedNodes(other.disconnectedNodes), currCycle(other.currCycle) {
    other.clear();
}

const Session &Session::operator=(const Session &other) {
    if (this != &other){
        clear();
        g = other.g;
        treeType = other.treeType;
        for (auto agent : other.agents) {
            addAgent(*agent);
        }
        occupiedNodes = other.occupiedNodes;
        infectedNodes = other.infectedNodes;
        disconnectedNodes = other.disconnectedNodes;
        currCycle = other.currCycle;
    }

    return *this;
}

const Session &Session::operator=(Session &&other)  {
    if (this != &other){
        clear();
        g = other.g;
        treeType = other.treeType;
        for (auto & agent : other.agents) {
            addAgent(*agent);
        }
        occupiedNodes = other.occupiedNodes;
        infectedNodes = other.infectedNodes;
        disconnectedNodes = other.disconnectedNodes;
        currCycle = other.currCycle;
        other.clear();
    }
    return *this;
}

Session::~Session() {
    clear();
}

void Session::clear(){
    for (auto & agent : agents) {
        delete agent;
    }
}

void Session::simulate() {

    while (!isTerminated()){//attack did not finish yet.
        const  int agentsNumber = agents.size();
        for (int i = 0; i < agentsNumber; i++) {//activating the agents to act.

            agents[i]->act(*this);

        }
        advanceCycle(); // advancing the cycle number.
    }

    //write to json file (final graph, list of infected nodes).
    WriteToJson();

}

void Session::addAgent(const Agent &agent) {
    agents.push_back(agent.clone());
}

void Session::setGraph(const Graph &graph) {
    g = graph;
}

Graph & Session::  GetGraph(){
    return g;
}

void Session::enqueueInfected(int node) {
    g.infectNode(node);
    infectedNodes.push(node);
}//done

int Session::dequeueInfected() {
    int output = infectedNodes.front();
    infectedNodes.pop();
    return output;
}//done


bool Session:: isTerminated (){
    bool  output = true;
    int numOfNodes = g.GetEdges().size();
    for (int i = 0; i < numOfNodes && output; ++i) {
        bool zeroVirus = true;
        bool fullyInfected = true;
        if (isInfected(i)){
            fullyInfected = true;
        }
        if (!isOccupied(i)){
            zeroVirus = true;
        }
        vector<int> ConnectedCompenent = g.DFS(i);
        for (unsigned int j = 1; j < ConnectedCompenent.size(); ++j) {
            if (!isInfected(ConnectedCompenent[j])){
                fullyInfected = false;
            }
            if (isOccupied(ConnectedCompenent[j])){
                zeroVirus = false;
            }
            if (!fullyInfected && !zeroVirus){
                output = false;
            }
        }
    }
    return output;
}

TreeType Session::getTreeType() const {
    return treeType;
}//done

int Session:: getCurrCycle() const{
    return currCycle;
} // done

void Session:: advanceCycle(){
    currCycle = currCycle+1;
} //done

void Session:: setAsOccupied(int node){
    occupiedNodes[node] = true;
}//done

bool Session::isOccupied(int node){
    return occupiedNodes[node];
}//doneisInfec

bool Session::isInfected(int node){
    return g.isInfected(node);
}//done

void Session:: WriteToJson(){
    json output;
    std::vector<int> infected = g.getInfectedNodes();//should add the infected nodes

    output["infectedNodes"]  = infected;
    output["graph"] = g.GetEdges();
    std::ofstream i ("output.json");
    i << output;
}

void Session::Disconnect(int node) {
    std::vector<std::vector<int>> edges = g.GetEdges();
    for (unsigned int i = 0; i < edges.size() ; i++) {
        if (edges[node][i] ==1){
            edges[node][i] = 0;
            edges[i][node] = 0;
        }
    }
    g.SetEdges(edges);
    disconnectedNodes[node] = true;
}

bool Session::isDisconnected(int node){
    return disconnectedNodes[node];
}

bool Session::ThereIsInfected() {
    return !infectedNodes.empty();
}