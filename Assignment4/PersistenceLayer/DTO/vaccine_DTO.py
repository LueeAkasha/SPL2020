# -*- coding: utf-8 -*-
"""
Created on Mon Jan 11 13:27:16 2021

@author: luee
"""

class vaccine_DTO:
    
    def __init__(self, _id, _date, _supplier, _quantity):
        self._id = _id
        self._date = _date
        self._supplier = _supplier
        self._quantity = _quantity
        pass
    
    def get_id(self):
        return self._id
    
    def get_date(self):
        return self._date
    
    def get_supplier(self):
        return self._supplier
    
    def get_quantity(self):
        return self._quantity
    
    def set_id(self, new_id):
        self._id = new_id
    
    def set_date(self, new_date):
        self._date = new_date
    
    def set_supplier(self, new_supplier):
        self._supplier = new_supplier
    
    def set_quantity(self, new_quantity):
        self._quantity = new_quantity