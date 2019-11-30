import 'package:flutter/services.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:ad/ad.dart';

void main() {
  const MethodChannel channel = MethodChannel('ad');

  setUp(() {
    channel.setMockMethodCallHandler((MethodCall methodCall) async {
      return '42';
    });
  });

  tearDown(() {
    channel.setMockMethodCallHandler(null);
  });

  test('getPlatformVersion', () async {
    expect(await Ad.platformVersion, '42');
  });
}
