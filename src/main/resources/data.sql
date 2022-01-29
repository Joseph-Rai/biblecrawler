INSERT INTO URL (name, domain)
VALUES ('You Version Bible', 'www.bible.com'),
       ('Holy Bible', 'www.holybible.or.kr'),
       ('Resurse Cristine', 'biblia.resursecristine.ro');

INSERT INTO book_name (book_name_id, youversion, resursecrestine)
VALUES (1, 'GEN', 'Geneza'),
       (2, 'EXO', 'Exodul'),
       (3, 'LEV', 'Leviticul'),
       (4, 'NUM', 'Numeri'),
       (5, 'DEU', 'Deuteronomul'),
       (6, 'JOS', 'Iosua'),
       (7, 'JDG', 'Judecatorii'),
       (8, 'RUT', 'Rut'),
       (9, '1SA', '1 Samuel'),
       (10, '2SA', '2 Samuel'),
       (11, '1KI', '1 Imparati'),
       (12, '2KI', '2 Imparati'),
       (13, '1CH', '1 Cronici'),
       (14, '2CH', '2 Cronici'),
       (15, 'EZR', 'Ezra'),
       (16, 'NEH', 'Neemia'),
       (17, 'EST', 'Estera'),
       (18, 'JOB', 'Iov'),
       (19, 'PSA', 'Psalmii'),
       (20, 'PRO', 'Proverbele'),
       (21, 'ECC', 'Eclesiastul'),
       (22, 'SNG', 'Cantarea cantarilor'),
       (23, 'ISA', 'Isaia'),
       (24, 'JER', 'Ieremia'),
       (25, 'LAM', 'Plangerile lui Ieremia'),
       (26, 'EZK', 'Ezechiel'),
       (27, 'DAN', 'Daniel'),
       (28, 'HOS', 'Osea'),
       (29, 'JOL', 'Ioel'),
       (30, 'AMO', 'Amos'),
       (31, 'OBA', 'Obadia'),
       (32, 'JON', 'Iona'),
       (33, 'MIC', 'Mica'),
       (34, 'NAM', 'Naum'),
       (35, 'HAB', 'Habacuc'),
       (36, 'ZEP', 'Tefania'),
       (37, 'HAG', 'Hagai'),
       (38, 'ZEC', 'Zaharia'),
       (39, 'MAL', 'Maleahi'),
       (40, 'MAT', 'Matei'),
       (41, 'MRK', 'Marcu'),
       (42, 'LUK', 'Luca'),
       (43, 'JHN', 'Ioan'),
       (44, 'ACT', 'Faptele apostolilor'),
       (45, 'ROM', 'Romani'),
       (46, '1CO', '1 Corinteni'),
       (47, '2CO', '2 Corinteni'),
       (48, 'GAL', 'Galateni'),
       (49, 'EPH', 'Efeseni'),
       (50, 'PHP', 'Filipeni'),
       (51, 'COL', 'Coloseni'),
       (52, '1TH', '1 Tesaloniceni'),
       (53, '2TH', '2 Tesaloniceni'),
       (54, '1TI', '1 Timotei'),
       (55, '2TI', '2 Timotei'),
       (56, 'TIT', 'Tit'),
       (57, 'PHM', 'Filimon'),
       (58, 'HEB', 'Evrei'),
       (59, 'JAS', 'Iacov'),
       (60, '1PE', '1 Petru'),
       (61, '2PE', '2 Petru'),
       (62, '1JN', '1 Ioan'),
       (63, '2JN', '2 Ioan'),
       (64, '3JN', '3 Ioan'),
       (65, 'JUD', 'Iuda'),
       (66, 'REV', 'Apocalipsa');

INSERT INTO bible_index (bible_index_id, book, max_chapter, book_name_id)
VALUES (1, '창세기', 50, 1),
       (2, '출애굽기', 40, 2),
       (3, '레위기', 27, 3),
       (4, '민수기', 36, 4),
       (5, '신명기', 34, 5),
       (6, '여호수아', 24, 6),
       (7, '사사기', 21, 7),
       (8, '룻기', 4, 8),
       (9, '사무엘상', 31, 9),
       (10, '사무엘하', 24, 10),
       (11, '열왕기상', 22, 11),
       (12, '열왕기하', 25, 12),
       (13, '역대상', 29, 13),
       (14, '역대하', 36, 14),
       (15, '에스라', 10, 15),
       (16, '느헤미야', 13, 16),
       (17, '에스더', 10, 17),
       (18, '욥기', 42, 18),
       (19, '시편', 150, 19),
       (20, '잠언', 31, 20),
       (21, '전도서', 12, 21),
       (22, '아가', 8, 22),
       (23, '이사야', 66, 23),
       (24, '예레미야', 52, 24),
       (25, '예레미야 애가', 5, 25),
       (26, '에스겔', 48, 26),
       (27, '다니엘', 12, 27),
       (28, '호세아', 14, 28),
       (29, '요엘', 3, 29),
       (30, '아모스', 9, 30),
       (31, '오바댜', 1, 31),
       (32, '요나', 4, 32),
       (33, '미가', 7, 33),
       (34, '나훔', 3, 34),
       (35, '하박국', 3, 35),
       (36, '스바냐', 3, 36),
       (37, '학개', 2, 37),
       (38, '스가랴', 14, 38),
       (39, '말라기', 4, 39),
       (40, '마태복음', 28, 40),
       (41, '마가복음', 16, 41),
       (42, '누가복음', 24, 42),
       (43, '요한복음', 21, 43),
       (44, '사도행전', 28, 44),
       (45, '로마서', 16, 45),
       (46, '고린도전서', 16, 46),
       (47, '고린도후서', 13, 47),
       (48, '갈라디아서', 6, 48),
       (49, '에베소서', 6, 49),
       (50, '빌립보서', 4, 50),
       (51, '골로새서', 4, 51),
       (52, '데살로니가전서', 5, 52),
       (53, '데살로니가후서', 3, 53),
       (54, '디모데전서', 6, 54),
       (55, '디모데후서', 4, 55),
       (56, '디도서', 3, 56),
       (57, '빌레몬서', 1, 57),
       (58, '히브리서', 13, 58),
       (59, '야고보서', 5, 59),
       (60, '베드로전서', 5, 60),
       (61, '베드로후서', 3, 61),
       (62, '요한일서', 5, 62),
       (63, '요한이서', 1, 63),
       (64, '요한삼서', 1, 64),
       (65, '유다서', 1, 65),
       (66, '요한계시록', 22, 66);