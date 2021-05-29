INSERT INTO USERS(userid, username, salt, password, firstname, lastname) VALUES (1, 'test', '9d+nWi7vqvm+blQqUMMNhQ==', 'i8CzTohDyGcxE2jmU59q4A==', 'test', 'test');
INSERT INTO FILES(filename, contenttype, filesize, userid) VALUES ('test01.pdf', 'pdf', '2MB', 1);
INSERT INTO FILES(filename, contenttype, filesize, userid) VALUES ('test02.pdf', 'pdf', '4MB', 1);
INSERT INTO NOTES(notetitle, notedescription, userid) VALUES ( 'My first note', 'This is a test note 01', 1 );
INSERT INTO NOTES(notetitle, notedescription, userid) VALUES ( 'My second note', 'This is a test note 02', 1 );
INSERT INTO CREDENTIALS(url, username, key, password, userid)  VALUES ( 'www.google.com', 'canessa01', 'bbUr2BtPpKV1uDGf1BYRlg==', '6yAiolaUihMYQbjLq8nJhg==', 1 );
INSERT INTO CREDENTIALS(url, username, key, password, userid)  VALUES ( 'www.facebook.com', 'canessa02', 'bbUr2BtPpKV1uDGf1BYRlg==', '6yAiolaUihMYQbjLq8nJhg==', 1 );
