--------------------------------------------------------
--  DDL for Table USERS
--------------------------------------------------------

  CREATE TABLE "USERS" 
   (	
    "USER_ID" NUMBER(21,0), 
	"FIRST_NAME" VARCHAR2(20 BYTE), 
	"MIDDLE_NAME" VARCHAR2(20 BYTE), 
	"LAST_NAME" VARCHAR2(20 BYTE), 
	"NICKNAME" VARCHAR2(20 BYTE), 
	"EMAIL" VARCHAR2(50 BYTE), 
	"BIRTHDATE" DATE, 
	"USERNAME" VARCHAR2(20 BYTE)
   );

Insert into USERS (USER_ID,FIRST_NAME,MIDDLE_NAME,LAST_NAME,NICKNAME,EMAIL,BIRTHDATE,USERNAME) values (1,'Earl','Dela Cruz','Tormes','Earl','earljann.tormes@cognizant.com',to_date('11-AUG-14','DD-MON-RR'),'etormes');
Insert into USERS (USER_ID,FIRST_NAME,MIDDLE_NAME,LAST_NAME,NICKNAME,EMAIL,BIRTHDATE,USERNAME) values (2,'Roberto',null,'Mabini','Robert','roberto.mabini@cognizant.com',to_date('11-AUG-14','DD-MON-RR'),'rmabini');
Insert into USERS (USER_ID,FIRST_NAME,MIDDLE_NAME,LAST_NAME,NICKNAME,EMAIL,BIRTHDATE,USERNAME) values (3,'Kaye','Garcia','Ramos','Baluga','katherine.ramos@cognizant.com',to_date('11-AUG-14','DD-MON-RR'),'kramos');
--------------------------------------------------------
--  Constraints for Table USERS
--------------------------------------------------------

ALTER TABLE "USERS" MODIFY ("USERNAME" NOT NULL ENABLE);
ALTER TABLE "USERS" MODIFY ("USER_ID" NOT NULL ENABLE);
ALTER TABLE "USERS" ADD CONSTRAINT "USER_ID_PK" PRIMARY KEY ("USER_ID") ENABLE;
  
--------------------------------------------------------
--  DDL for Index USERS_UK
--------------------------------------------------------

CREATE UNIQUE INDEX "USERS_UK" ON "USERS" ("USERNAME"); 
  
  --------------------------------------------------------
--  DDL for Sequence USERS_SEQ
--------------------------------------------------------

CREATE SEQUENCE  "USERS_SEQ"  MINVALUE 4 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 1 CACHE 20 NOORDER  NOCYCLE ;

 