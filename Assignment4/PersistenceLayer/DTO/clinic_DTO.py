# -*- coding: utf-8 -*-
"""
Created on Mon Jan 11 13:27:55 2021

@author: luee
"""

class clinic_DTO:
   
    def __init__(self, _id, _location, _demand, _logistic):
        self._id = _id
        self._location = _location
        self._demand = _demand
        self._logistic = _logistic
        pass
    
    def get_id(self):
        return self._id
    
    def get_location(self):
        return self._location
    
    def get_logistic(self):
        return self._logistic
    
    def get_demand(self):
        return self._demand
    
    def set_id(self, new_id):
        self._id = new_id
    
    def set_location(self, new_location):
        self._location = new_location
    
    def set_logistic(self, new_logistic):
        self._logistic = new_logistic
        
    def set_demand(self, new_demand):
        self._demand = new_demand