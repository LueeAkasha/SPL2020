//
// Created by zoix on 14/11/2020.
//
#include <iostream>
#include "../include/Agent.h"
#include "../include/Tree.h"

using  namespace std;

Virus::Virus(int nodeInd) : Agent(), nodeInd(nodeInd) {

} // done

ContactTracer *ContactTracer::clone() const {
    return  new ContactTracer(*this);
}

Virus *Virus::clone() const {
    return new Virus(*this);
}

void ContactTracer::act(Session &session) {
        if (session.ThereIsInfected()) {
            int infectedNode = session.dequeueInfected();

                Tree *tree = session.GetGraph().BFS(session, infectedNode);
                int nodeToDeal = tree->traceTree();
                session.Disconnect(nodeToDeal);
                delete tree;
        }

}

void Virus::act(Session &session) {

    if (session.isDisconnected(nodeInd)){
        if (!session.isInfected(this->nodeInd)){
            session.enqueueInfected(this->nodeInd);
        }
        return;
    }
    if (!session.isInfected(this->nodeInd)){
        session.enqueueInfected(this->nodeInd);

    }

   std::vector<int> neighbors = session.GetGraph().getNeighbors(this->nodeInd);

    for (unsigned int i = 0; i < neighbors.size(); i++){
        if (!session.isOccupied(neighbors[i])){
            session.setAsOccupied(neighbors[i]);
            Virus v(neighbors[i]);
            session.addAgent(v);
            return;
        }
    }
}





