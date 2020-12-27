#ifndef AGENT_H_
#define AGENT_H_

#include <vector>
#include "Session.h"

class Agent{
public:
    Agent() = default; // constructor.
    virtual ~Agent() = default; // destructor.
    virtual  Agent * clone() const=0; // abstract cloning function.
    virtual void act(Session& session)=0; // abstract agent acting function.
};

class ContactTracer: public Agent{
public:
    ContactTracer() = default; // constructor.
    virtual ~ContactTracer() = default; //destructor.
    virtual ContactTracer* clone() const; // ContactTracer cloning.
    virtual void act(Session& session); // ContactTracer acting.
};


class Virus: public Agent{
public:
    Virus(int nodeInd); // constructor.
    virtual ~Virus() = default; //destructor.
    virtual Virus* clone() const; // Virus cloning.
    virtual void act(Session& session); //Virus acting.

private:
    const int nodeInd; // the occupied node.
};

#endif
