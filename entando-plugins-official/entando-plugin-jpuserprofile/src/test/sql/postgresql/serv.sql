CREATE TABLE jpuserprofile_authuserprofiles
(
  username character varying(40) NOT NULL,
  profiletype character varying(30) NOT NULL,
  "xml" character varying NOT NULL,
  publicprofile smallint NOT NULL,
  CONSTRAINT jpuserprofile_autuserprofiles_pkey PRIMARY KEY (username)
);

CREATE TABLE jpuserprofile_profilesearch
(
  username character varying(40) NOT NULL,
  attrname character varying(30) NOT NULL,
  textvalue character varying(255),
  datevalue date,
  numvalue integer,
  langcode character varying(2),
  CONSTRAINT jpuserprofile_profilesearch_username_fkey FOREIGN KEY (username)
      REFERENCES jpuserprofile_authuserprofiles (username) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

INSERT INTO jpuserprofile_authuserprofiles (username, profiletype, xml, publicprofile) VALUES ('editorCustomers', 'PFL', '<?xml version="1.0" encoding="UTF-8"?>
<profile id="editorCustomers" typecode="PFL" typedescr="Profilo utente/cittadino tipo"><descr /><groups /><categories />
	<attributes>
		<attribute name="Name" attributetype="Monotext"><monotext>Sean</monotext></attribute>
		<attribute name="Surname" attributetype="Monotext"><monotext>Red</monotext></attribute>
		<attribute name="birthdate" attributetype="Date"><date>19520521</date></attribute>
		<attribute name="email" attributetype="Monotext"><monotext>sean.red@mailinator.com</monotext></attribute>
		<attribute name="language" attributetype="Monotext"><monotext>it</monotext></attribute>
		<attribute name="boolean1" attributetype="Boolean"><boolean>false</boolean></attribute>
		<attribute name="boolean2" attributetype="Boolean"><boolean>false</boolean></attribute>
	</attributes>
</profile>', 0);
INSERT INTO jpuserprofile_authuserprofiles (username, profiletype, xml, publicprofile) VALUES ('mainEditor', 'PFL', '<?xml version="1.0" encoding="UTF-8"?>
<profile id="editorCustomers" typecode="PFL" typedescr="Profilo utente/cittadino tipo"><descr /><groups /><categories />
	<attributes>
		<attribute name="Name" attributetype="Monotext"><monotext>Amanda</monotext></attribute>
		<attribute name="Surname" attributetype="Monotext"><monotext>Chedwase</monotext></attribute>
		<attribute name="birthdate" attributetype="Date"><date>19471124</date></attribute>
		<attribute name="email" attributetype="Monotext"><monotext>amanda.chedwase@mailinator.com</monotext></attribute>
		<attribute name="language" attributetype="Monotext"><monotext>it</monotext></attribute>
		<attribute name="boolean1" attributetype="Boolean"><boolean>false</boolean></attribute>
		<attribute name="boolean2" attributetype="Boolean"><boolean>false</boolean></attribute>
	</attributes>
</profile>', 0);
INSERT INTO jpuserprofile_authuserprofiles (username, profiletype, xml, publicprofile) VALUES ('pageManagerCoach', 'PFL', '<?xml version="1.0" encoding="UTF-8"?>
<profile id="editorCustomers" typecode="PFL" typedescr="Profilo utente/cittadino tipo"><descr /><groups /><categories />
	<attributes>
		<attribute name="Name" attributetype="Monotext"><monotext>Raimond</monotext></attribute>
		<attribute name="Surname" attributetype="Monotext"><monotext>Stevenson</monotext></attribute>
		<attribute name="birthdate" attributetype="Date"><date>20000904</date></attribute>
		<attribute name="email" attributetype="Monotext"><monotext>raimond.stevenson@mailinator.com</monotext></attribute>
		<attribute name="language" attributetype="Monotext"><monotext>it</monotext></attribute>
		<attribute name="boolean1" attributetype="Boolean"><boolean>false</boolean></attribute>
		<attribute name="boolean2" attributetype="Boolean"><boolean>false</boolean></attribute>
	</attributes>
</profile>', 0);
INSERT INTO jpuserprofile_authuserprofiles (username, profiletype, xml, publicprofile) VALUES ('editorCoach', 'PFL', '<?xml version="1.0" encoding="UTF-8"?>
<profile id="editorCoach" typecode="PFL" typedescr="Profilo utente/cittadino tipo"><descr /><groups /><categories />
	<attributes>
		<attribute name="Name" attributetype="Monotext"><monotext>Rick</monotext></attribute>
		<attribute name="Surname" attributetype="Monotext"><monotext>Bobonsky</monotext></attribute>
		<attribute name="email" attributetype="Monotext"><monotext>rick.bobonsky@mailinator.com</monotext></attribute>
		<attribute name="birthdate" attributetype="Date"><date>19450301</date></attribute>
		<attribute name="language" attributetype="Monotext"><monotext>it</monotext></attribute>
		<attribute name="boolean1" attributetype="Boolean"><boolean>false</boolean></attribute>
		<attribute name="boolean2" attributetype="Boolean"><boolean>false</boolean></attribute>
	</attributes>
</profile>', 0);

INSERT INTO jpuserprofile_profilesearch (username, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('editorCoach', 'Name', 'Rick', NULL, NULL, NULL);
INSERT INTO jpuserprofile_profilesearch (username, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('editorCoach', 'Surname', 'Bobonsky', NULL, NULL, NULL);
INSERT INTO jpuserprofile_profilesearch (username, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('editorCoach', 'email', 'rick.bobonsky@mailinator.com', NULL, NULL, NULL);
INSERT INTO jpuserprofile_profilesearch (username, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('editorCoach', 'birthdate', NULL, '1945-03-01', NULL, NULL);
INSERT INTO jpuserprofile_profilesearch (username, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('editorCoach', 'boolean1', 'false', NULL, NULL, NULL);
INSERT INTO jpuserprofile_profilesearch (username, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('editorCoach', 'boolean2', 'false', NULL, NULL, NULL);
INSERT INTO jpuserprofile_profilesearch (username, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('editorCustomers', 'Name', 'Sean', NULL, NULL, NULL);
INSERT INTO jpuserprofile_profilesearch (username, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('editorCustomers', 'Surname', 'Red', NULL, NULL, NULL);
INSERT INTO jpuserprofile_profilesearch (username, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('editorCustomers', 'email', 'sean.red@mailinator.com', NULL, NULL, NULL);
INSERT INTO jpuserprofile_profilesearch (username, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('editorCustomers', 'birthdate', NULL, '1952-05-21', NULL, NULL);
INSERT INTO jpuserprofile_profilesearch (username, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('editorCustomers', 'boolean1', 'false', NULL, NULL, NULL);
INSERT INTO jpuserprofile_profilesearch (username, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('editorCustomers', 'boolean2', 'false', NULL, NULL, NULL);
INSERT INTO jpuserprofile_profilesearch (username, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('mainEditor', 'Name', 'Amanda', NULL, NULL, NULL);
INSERT INTO jpuserprofile_profilesearch (username, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('mainEditor', 'Surname', 'Chedwase', NULL, NULL, NULL);
INSERT INTO jpuserprofile_profilesearch (username, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('mainEditor', 'email', 'amanda.chedwase@mailinator.com', NULL, NULL, NULL);
INSERT INTO jpuserprofile_profilesearch (username, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('mainEditor', 'birthdate', NULL, '1947-11-24', NULL, NULL);
INSERT INTO jpuserprofile_profilesearch (username, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('mainEditor', 'boolean1', 'false', NULL, NULL, NULL);
INSERT INTO jpuserprofile_profilesearch (username, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('mainEditor', 'boolean2', 'false', NULL, NULL, NULL);
INSERT INTO jpuserprofile_profilesearch (username, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('pageManagerCoach', 'Name', 'Raimond', NULL, NULL, NULL);
INSERT INTO jpuserprofile_profilesearch (username, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('pageManagerCoach', 'Surname', 'Stevenson', NULL, NULL, NULL);
INSERT INTO jpuserprofile_profilesearch (username, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('pageManagerCoach', 'email', 'raimond.stevenson@mailinator.com', NULL, NULL, NULL);
INSERT INTO jpuserprofile_profilesearch (username, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('pageManagerCoach', 'birthdate', NULL, '2000-09-04', NULL, NULL);
INSERT INTO jpuserprofile_profilesearch (username, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('pageManagerCoach', 'boolean1', 'false', NULL, NULL, NULL);
INSERT INTO jpuserprofile_profilesearch (username, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('pageManagerCoach', 'boolean2', 'false', NULL, NULL, NULL);

