# -*- coding: utf-8 -*-
"""
Created on Mon Jan 11 13:27:55 2021

@author: luee
"""

class supplier_DTO:
 
    def __init__(self, _id, _name, _logistic):
        self._id = _id
        self._name = _name
        self._logistic = _logistic
        pass
    
    def get_id(self):
        return self._id
    
    def get_name(self):
        return self._name
    
    def get_logistic(self):
        return self._logistic
    
    def set_id(self, new_id):
        self._id = new_id
    
    def set_name(self, new_name):
        self._name = new_name
    
    def set_logistic(self, new_logistic):
        self._logistic = new_logistic
    