import { WebPlugin } from '@capacitor/core';

import type { FloatingWindowPlugin } from './definitions';

export class FloatingWindowWeb
  extends WebPlugin
  implements FloatingWindowPlugin {
  async echo(options: { value: string }): Promise<{ value: string }> {
    console.log('ECHO', options);
    return options;
  }
}
