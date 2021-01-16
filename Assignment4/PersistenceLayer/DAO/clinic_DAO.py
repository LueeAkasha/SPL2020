# -*- coding: utf-8 -*-
"""
Created on Mon Jan 11 13:27:55 2021

@author: luee
"""
from PersistenceLayer.DTO.clinic_DTO import clinic_DTO
class _clinic_DAO:
    def __init__(self, conn):
        self._conn = conn


    def insert(self, clinic):
        try:
            self._conn.execute("""
                INSERT INTO clinics (id, location, demand, logistic) VALUES (?, ?, ?, ?);
            """, [clinic.get_id(), clinic.get_location(), clinic.get_demand(), clinic.get_logistic()])
            self._conn.commit()
        except Exception as error:
            print(error)

    
    def update_demand(self, location, amount):  # should support it with some triggers before and after about the case of ZERO
        try:
            self._conn.execute(""" 
                UPDATE clinics
                SET demand = demand - ? 
                WHERE location = ?                               
           ; """, [amount, location])
            self._conn.commit()
        except Exception as error:
            print(error)
            
    def get_clinic_by_location(self, location):
        clinic = None
        try:
            cursor = self._conn.cursor() 
            clinic_row = cursor.execute(""" SELECT * FROM clinics WHERE location=? ;""",[location]).fetchone()
            clinic = clinic_DTO(clinic_row[0], clinic_row[1], clinic_row[2], clinic_row[3])
            cursor.close()
        except Exception as error:
            print(error)
        return clinic