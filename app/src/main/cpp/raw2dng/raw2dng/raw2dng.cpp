/* Copyright (C) 2015 Fimagena

   This library is free software; you can redistribute it and/or
   modify it under the terms of the GNU Library General Public   
   License as published by the Free Software Foundation; either
   version 2 of the License, or (at your option) any later version.
   
   This library is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
   Library General Public License for more details.
   
   You should have received a copy of the GNU Library General Public License
   along with this library; see the file COPYING.  If not, write to
   the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor,
   Boston, MA 02110-1301, USA.
*/

#include <stdexcept>
#include <iostream>
#include <exiv2/exiv2.hpp>

#include "raw2dng.h"
#include "rawConverter.h"


void publishProgressUpdate(const char *message) {std::cout << " - " << message << "...\n";}


void registerPublisher(std::function<void(const char*)> function) {RawConverter::registerPublisher(function);}


void raw2dng(std::string rawFilename, std::string outFilename, std::string dcpFilename, bool embedOriginal) {
    Exiv2::enableBMFF();

    RawConverter converter;
    converter.openRawFile(rawFilename);
    converter.buildNegative(dcpFilename);
    if (embedOriginal) converter.embedRaw(rawFilename);
    converter.renderImage();
    converter.renderPreviews();
    converter.writeDng(outFilename);
}


void raw2tiff(std::string rawFilename, std::string outFilename, std::string dcpFilename) {
    RawConverter converter;
    converter.openRawFile(rawFilename);
    converter.buildNegative(dcpFilename);
    converter.renderImage();
    converter.renderPreviews();
    converter.writeTiff(outFilename);
}


void raw2jpeg(std::string rawFilename, std::string outFilename, std::string dcpFilename) {
    RawConverter converter;
    converter.openRawFile(rawFilename);
    converter.buildNegative(dcpFilename);
    converter.renderImage();
    converter.writeJpeg(outFilename);
}
