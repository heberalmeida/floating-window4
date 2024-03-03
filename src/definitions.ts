export interface FloatingWindowPlugin {
  showFloatingWindow(options: { url: string }): Promise<{ url: string }>;
  closeFloatingWindow(): Promise<null>;
}
