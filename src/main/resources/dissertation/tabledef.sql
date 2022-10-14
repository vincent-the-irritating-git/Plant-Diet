CREATE TABLE person(
height int DEFAULT 0,
weight double DEFAULT 0,
calorie_goal int DEFAULT 0
);

CREATE TABLE item(
itemID integer PRIMARY KEY,
name varchar(50) NOT NULL,
shop varchar(30),
isVegan int NOT NULL DEFAULT 0,
amount varchar(50) CHECK(amount IN('bag','tin','spoon','bottle','can/whole
bottle','handful bag','loose')),
calories int,
type varchar (40) CHECK(type IN('bread','cakes','fruit, veg and pulses',
'"meat"',
'non-alcoholic','alcohol','cereal','chocolate, crisps and biscuits','nuts',
'frozen
food','ready meals','pasta','cheese','milk and eggs','spread','sugar and
preserves','sauces, oil and dressings','seasoning','snacks'))
);

CREATE TABLE dish(
dishID integer PRIMARY KEY,
name varchar(50) NOT NULL,
amount_eaten int DEFAULT 100 CHECK (amount_eaten>0),
preset_calories integer CHECK (preset_calories>0)
);

CREATE TABLE itemDish(
dish integer NOT NULL,
item int NOT NULL,
setAmount int,
FOREIGN KEY (dish) REFERENCES dish(dishID),
FOREIGN KEY (item) REFERENCES item(itemID)
);

CREATE TABLE calories (
dish int,
meal varchar(9) CHECK(meal IN('breakfast','dinner','tea','snack','pudding')), 
date text DEFAULT (date('now')),
calories int NOT NULL,
portion int CHECK (portion>0),
FOREIGN KEY (dish) REFERENCES dish(dishID)
);

CREATE TABLE weight(
weight int NOT NULL CHECK(weight>0),
date text DEFAULT (date('now'))
);

CREATE TABLE tempDish(
item int NOT NULL UNIQUE,
setCalories int,
setAmount string,
FOREIGN KEY (item) REFERENCES item(itemID)
);

