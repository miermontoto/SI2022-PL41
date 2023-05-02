class OnlyTime
    attr_accessor :hours, :minutes, :seconds

    def initialize(hours, minutes = nil, seconds = nil)
        @hours = hours
        @minutes = minutes == nil ? 0 : minutes
        @seconds = seconds == nil ? 0 : seconds
    end

    def to_s
        return "#{"%02d" % @hours}:#{"%02d" % @minutes}:#{"%02d" % @seconds}"
    end
end

def phonify(number)
    number.tr('-','').tr(' ','').tr('.', '')
end

def add_data_to_sql(data, table)
    sql = "INSERT INTO #{table} ("

    data[0].instance_variables.each do |var|
        sql += "#{var[1..-1]}, "
    end

    sql = sql[0..-3] + ") VALUES\n"
    data.each do |d|
        sql += "("
        data[0].instance_variables.each do |var|
            sql += "'#{d.instance_variable_get(var)}', "
        end
        sql = sql[0..-3] + "),\n"
    end
    sql = sql[0..-3] + ";\n\n"
end
