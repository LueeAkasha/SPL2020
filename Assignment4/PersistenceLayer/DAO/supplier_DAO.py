# -*- coding: utf-8 -*-
"""
Created on Mon Jan 11 13:27:55 2021

@author: luee
"""
from PersistenceLayer.DTO.supplier_DTO import supplier_DTO
class _supplier_DAO:
    def __init__(self, conn):
        self._conn = conn
        pass
    
    def insert(self, supplier):
        try:
            self._conn.execute("""
                INSERT INTO suppliers (id, name, logistic) VALUES (?, ?, ?);
            """, [supplier.get_id(), supplier.get_name(), supplier.get_logistic()])
            self._conn.commit()
        except Exception as error:
            print(error)
            
    def get_supplier_by_id(self, _id):
        supplier = None
        try:
            cursor = self._conn.cursor() 
            sup_row = cursor.execute(""" SELECT * FROM suppliers WHERE id=? ;""",[_id]).fetchone()
            supplier = supplier_DTO(sup_row[0], sup_row[1], sup_row[2])
            cursor.close()
        except Exception as error:
            print(error)
        return supplier
    
    def get_supplier_by_name(self, name):
        supplier = None
        try:
            cursor = self._conn.cursor() 
            sup_row = cursor.execute(""" SELECT * FROM suppliers WHERE name=? ;""",[name]).fetchone()
            supplier = supplier_DTO(sup_row[0], sup_row[1], sup_row[2])
            cursor.close()
        except Exception as error:
            print(error)
        return supplier
        
