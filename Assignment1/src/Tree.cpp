//
// Created by zoix on 14/11/2020.
//
#include <iostream>
#include "../include/Tree.h"
#include "../include/Session.h"
using namespace std;

Tree::Tree(int rootLabel) : node(rootLabel), children(std::vector<Tree*>()) {

}

Tree::Tree(const Tree &other) : node(other.getNode()), children(other.children){
}


Tree::Tree(Tree &&other) : node(other.getNode()), children(other.getChildren()) {
    other.clear();

}

const Tree &Tree::operator=(const Tree &other) {
    if (this != &other){
        clear();
        node = other.getNode();
        for (auto i : other.children) {
            addChild(*i);
        }
    }

    return *this;
}

const Tree &Tree::operator=(Tree &&other) {
    if (this != &other){
        clear();
        node = other.getNode();
        for (auto i : other.children) {
            addChild(*i);
        }
        other.clear();
    }
    return *this;
}

Tree::~Tree() {
    clear();
}

void Tree::clear(){
    for(Tree *child : children){
        delete child;
    }
}

Tree *CycleTree::clone() const {
    return new CycleTree(*this);
} //done


Tree *MaxRankTree::clone() const {
    return new MaxRankTree(*this);
} //done

Tree * RootTree::clone() const {
    return new RootTree(*this);
} //done

int CycleTree::traceTree() {
    int output(RecursionTraceTree(currCycle));
    return output;
}
int MaxRankTree::traceTree() {
    int maxRanked = node;
    int maxRank = Ranking();
    queue<Tree *> nodes;
    nodes.push(this);
    while (!nodes.empty()){
        Tree *temp = nodes.front();
        nodes.pop();

        if (temp->Ranking() > maxRank){

            maxRanked = temp->getNode();
            maxRank = temp->Ranking();
        }
        for (int i = 0; i < temp->Ranking(); i++) {
            nodes.push((temp->getChild(i)));

        }
    }

    return maxRanked;
}


int RootTree::traceTree() {
    return node;
}

void Tree::addChild(const Tree &child) {
    Tree* clone = child.clone();
    children.push_back(clone);
}//done

Tree *Tree::createTree(const Session &session, int rootLabel) {
    TreeType treeType = session.getTreeType();
    if(treeType == Cycle){
        return new CycleTree(rootLabel, session.getCurrCycle());
    }
    else if (treeType == MaxRank){
        return new MaxRankTree(rootLabel);
    }
    else if (treeType == Root){
        return new RootTree(rootLabel);
    }
    return new RootTree(rootLabel);
}//done

int Tree:: Ranking(){
    return children.size();
}

int Tree:: getNode() const{
    return node;
}

Tree * Tree::getChild(int i){
    return children[i];
}

CycleTree::CycleTree(int rootLabel, int currCycle) : Tree(rootLabel), currCycle(currCycle) {

}//done
CycleTree::~CycleTree() = default;

MaxRankTree::MaxRankTree(int rootLabel) : Tree(rootLabel) {

}//done
MaxRankTree::~MaxRankTree() = default;

RootTree::RootTree(int rootLabel) : Tree(rootLabel)  {

}//done
RootTree::~RootTree() = default;

int Tree::RecursionTraceTree(int c){
    int output = node;
    if (c == 0 || children.empty()){
        return output;
    }
    return children[0]->RecursionTraceTree(c -1);
}//done




std::vector<Tree *> Tree::getChildren() {
    return children;
}//done


