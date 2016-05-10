package qotd
class Quote {
String text;
static belongsTo = [attribution:Attribution];
static constraints = {
text nullable: false
attribution nullable: true
}
}