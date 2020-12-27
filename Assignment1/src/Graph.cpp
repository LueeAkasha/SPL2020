//
// Created by zoix on 14/11/2020.
//

#include <iostream>
#include "../include/Graph.h"
#include "../include/Tree.h"

using namespace std;

Graph::Graph(std::vector<std::vector<int>> matrix) : edges(matrix), infectedNodes(std::queue<int>()) , isInfectedNodes(std::vector<bool>(matrix.size())) {
    init(matrix.size());
}

void Graph::init(int size) {
    for (int i = 0; i < size; ++i) {
        isInfectedNodes[i] = false;
    }
}//done

Graph::Graph(const Graph &other): edges(other.edges),infectedNodes(other.infectedNodes), isInfectedNodes(other.isInfectedNodes){
}//done

void Graph::infectNode(int nodeInd) {
    isInfectedNodes[nodeInd] = true;
    infectedNodes.push(nodeInd);
}//done

bool Graph::isInfected(int nodeInd) {
    return isInfectedNodes[nodeInd];
}//done

vector<int> Graph::getInfectedNodes() {
    vector<int> output;
    while (!infectedNodes.empty()){
        output.push_back(infectedNodes.front());
        infectedNodes.pop();
    }
    return output;
}//done

vector<vector<int>> Graph::GetEdges() {
    return edges;
}

Tree* Graph::BFS(Session &session, int startNode) {
    std::vector<bool> visited;
    for(unsigned int i = 0; i< edges.size(); i++){
        visited.push_back(false);
    }

    return BFS_helper(session, startNode, visited);
}

Tree * Graph::BFS_helper(Session &session, int startNode, vector<bool> visited) {
    visited[startNode] = true;
    Tree *outputTree = Tree::createTree(session, startNode);
    queue<Tree *> trees;
    trees.push(outputTree);
    while (!trees.empty()) {
        Tree *temp = trees.front();
        trees.pop();
        vector<int> neighbors = getNeighbors(temp->getNode());
        for (unsigned int i = 0; i < neighbors.size(); i++) {
            if (!visited[neighbors[i]]) {
                visited[neighbors[i]] = true;
                Tree * child = Tree::createTree(session, neighbors[i]);
                temp->addChild(*child);
                trees.push((temp->getChild(temp->Ranking() - 1)));
                delete child;
            }
        }
    }

    return outputTree;
}

vector<int> Graph::DFS(int startNode){
    vector<bool> visited(edges.size());
    for (unsigned int i = 0; i < visited.size(); ++i) {
        visited[i] = false;
    }

    return DFS_helper(startNode, visited);

}

vector<int> Graph::DFS_helper(int startNode, vector<bool> visited){
    visited[startNode] = true;
    vector<int> output;
    output.push_back(startNode);
    vector<int> neighbors = getNeighbors(startNode);
    for (unsigned int i = 0; i < neighbors.size(); ++i) {
        if (!visited[neighbors[i]]){
            vector<int> temp = DFS_helper(neighbors[i], visited);
            for (unsigned int j = 0; j < temp.size(); ++j) {
                output.push_back(temp[j]);
            }
        }
    }
    return output;
}

std::vector<int>Graph:: getNeighbors(int nodeInd){
    std::vector<int> output;
    int size = edges[nodeInd].size();
    for ( int i = 0 ; i< size ; i++) {
        if (edges[nodeInd][i] == 1 && i != nodeInd)
            output.push_back(i);
    }
    return output;
}

void Graph::SetEdges(std::vector<std::vector<int>> matrix) {
    edges = matrix;
}
