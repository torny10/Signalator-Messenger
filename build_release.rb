#!/usr/bin/ruby
require 'fileutils'


# unless system('./gradlew --stacktrace assemblePlayRelease')
#     puts "BUILD FAILED"
#     exit $?
# end

apk_dir="app/build/outputs/apk/play/release"

file_list = []
version = ""
Dir.each_child(apk_dir) {|name|
    m = /\S+play\S+release-(\S+).apk/.match name
    if m != nil
        version = m[1]
        file_list.push "#{apk_dir}/#{name}"
    end 
}
zip_name = "signalator-#{version}.zip"
FileUtils.rm_rf zip_name
cmd_zip = "zip -j #{zip_name} #{file_list.join ' '}"
puts cmd_zip
system(cmd_zip)