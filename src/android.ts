import { WebPlugin } from '@capacitor/core';

import type { FloatingWindowPlugin } from './definitions';

export class FloatingWindowAndroid
  extends WebPlugin
  implements FloatingWindowPlugin
{
  closeFloatingWindow(): Promise<any> {
    throw new Error('Method not implemented.');
  }
  async showFloatingWindow(options: { url: string }): Promise<{ url: string }> {
    console.log('showFloatingWindow', options);
    return options;
  }
}
