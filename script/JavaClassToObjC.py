input_file = "in.txt"
nscopy = False


type_map = {"int":"NSNumber*", "float":"NSNumber*", "double":"NSNumber*",\
            "long":"NSNumber*", "String":"NSString*", "boolean":"NSNumber*"}

class Field:
    '''
    Fields
    '''

    def __init__(self, name, sname, tname, readonly, nonnull):
        self.name = name
        self.sname = sname
        self.tname = tname
        self.readonly = readonly
        self.nonnull = nonnull

    def objc_type_name(self):
        if self.tname in type_map:
            return type_map[self.tname]
        else:
            return self.tname + "*"

    def declaration(self):
        ## @property (strong, nonatomic) UIView* container;
        readonly = ""
        if self.readonly:
            readonly = ", readonly"
        return "@property (strong, nonatomic{0}) {1} {2};".format(readonly, self.objc_type_name(), self.name)

    def parsing_line(self):
        
        t = '''id raw{1} = [rawJson objectForKey:@"{0}"];
if(!raw{1} || [raw{1} isEqual:[NSNull null]]) {{
    {4}
}} else {{
    _{3} = raw{1};
}}\n'''
        initline = "_{0} = nil;".format(self.name)
        if self.nonnull:
            initline = "_{0} = FILL".format(self.name)
        capname = self.name[0].upper() + self.name[1:]
        return t.format(self.sname, capname, self.objc_type_name(), self.name, initline)

    def copy_line(self):
        return "another.{0} = [self.{0} copyWithZone:zone];".format(self.name)

    def __repr__(self):
        return "<Field: {0} \"{1}\" {2} {3} {4}>".format(self.name, self.sname, self.readonly, self.nonnull, self.tname)

def tokenize(fname):
    f = open(fname, "r")
    lines = f.readlines()
    f.close()
    tokens = []
    for line in lines:
        tokens.extend(line.split())
    return tokens

def find_fields(tokens):
    fields = []
    i = 0
    while i < len(tokens):
        if tokens[i].startswith("@Read"):

            nonnull = False
            name = ""
            sname = ""
            tname = ""
            readonly = False

            while True:
                if tokens[i] == "@Readonly":
                    readonly = True
                if tokens[i] == "@NonNull":
                    nonnull = True
                if tokens[i].startswith("@SerializedName"):
                    sname = tokens[i].split('"')[1]
                if tokens[i].endswith(";"):
                    tname = tokens[i - 1]
                    name = tokens[i].strip(";")
                    name = name[1:]
                    name = name[0].lower() + name[1:]

                    field = Field(name, sname, tname, readonly, nonnull)
                    fields.append(field)
                    break;
                i += 1
        else:
            i += 1
    return fields

tokens = tokenize(input_file)
fields = find_fields(tokens)
for f in fields:
    print(f)

hout = open("out.h", "w")
for f in fields:
    line = f.declaration()
    hout.write(line)
    hout.write("\n")
hout.close()

mout = open("out.m", "w")
mout.write("// parsing function codes \n")
for f in fields:
    line = f.parsing_line()
    mout.write(line)
    mout.write("\n")

mout.write("\n\n// CopyWithZone codes\n")
for f in fields:
    line = f.copy_line()
    mout.write(line)
    mout.write("\n")

mout.close()
