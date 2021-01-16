# -*- coding: utf-8 -*-
"""
Created on Mon Jan 11 13:27:56 2021

@author: luee
"""

class logistic_DTO:
    
    def __init__(self, _id, _name, _count_sent, _count_received):
        self._id = _id
        self._name = _name
        self._count_sent = _count_sent
        self._count_received = _count_received
        pass
    
    def get_id(self):
        return self._id
    
    def get_name(self):
        return self._name
    
    def get_count_sent(self):
        return self._count_sent
    
    def get_count_received(self):
        return self._count_received
    
    def set_id(self, new_id):
        self._id = new_id
    
    def set_name(self, new_name):
        self._name = new_name
    
    def set_count_sent(self, new_count_sent):
        self._count_sent = new_count_sent
    
    def set_count_received(self, new_count_received):
        self._count_received = new_count_received